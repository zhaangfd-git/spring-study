//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.asm.Opcodes;
import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.expression.common.ExpressionUtils;
import com.zhangfd.spring.expression.spel.*;
import com.zhangfd.spring.lang.Nullable;
import com.zhangfd.spring.util.Assert;
import com.zhangfd.spring.util.ObjectUtils;


public abstract class SpelNodeImpl implements SpelNode, Opcodes {
    private static final SpelNodeImpl[] NO_CHILDREN = new SpelNodeImpl[0];
    private final int startPos;
    private final int endPos;
    protected SpelNodeImpl[] children;
    @Nullable
    private SpelNodeImpl parent;
    @Nullable
    protected volatile String exitTypeDescriptor;

    public SpelNodeImpl(int startPos, int endPos, SpelNodeImpl... operands) {
        this.children = NO_CHILDREN;
        this.startPos = startPos;
        this.endPos = endPos;
        if (!ObjectUtils.isEmpty(operands)) {
            this.children = operands;
            SpelNodeImpl[] var4 = operands;
            int var5 = operands.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                SpelNodeImpl operand = var4[var6];
                Assert.notNull(operand, "Operand must not be null");
                operand.parent = this;
            }
        }

    }

    protected boolean nextChildIs(Class<?>... classes) {
        if (this.parent != null) {
            SpelNodeImpl[] peers = this.parent.children;
            int i = 0;

            for(int max = peers.length; i < max; ++i) {
                if (this == peers[i]) {
                    if (i + 1 >= max) {
                        return false;
                    }

                    Class<?> peerClass = peers[i + 1].getClass();
                    Class[] var6 = classes;
                    int var7 = classes.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                        Class<?> desiredClass = var6[var8];
                        if (peerClass == desiredClass) {
                            return true;
                        }
                    }

                    return false;
                }
            }
        }

        return false;
    }

    @Nullable
    public final Object getValue(ExpressionState expressionState) throws EvaluationException {
        return this.getValueInternal(expressionState).getValue();
    }

    public final TypedValue getTypedValue(ExpressionState expressionState) throws EvaluationException {
        return this.getValueInternal(expressionState);
    }

    public boolean isWritable(ExpressionState expressionState) throws EvaluationException {
        return false;
    }

    public void setValue(ExpressionState expressionState, @Nullable Object newValue) throws EvaluationException {
        throw new SpelEvaluationException(this.getStartPosition(), SpelMessage.SETVALUE_NOT_SUPPORTED, new Object[]{this.getClass()});
    }

    public SpelNode getChild(int index) {
        return this.children[index];
    }

    public int getChildCount() {
        return this.children.length;
    }

    @Nullable
    public Class<?> getObjectClass(@Nullable Object obj) {
        if (obj == null) {
            return null;
        } else {
            return obj instanceof Class ? (Class)obj : obj.getClass();
        }
    }

    public int getStartPosition() {
        return this.startPos;
    }

    public int getEndPosition() {
        return this.endPos;
    }

    public boolean isCompilable() {
        return false;
    }

    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        throw new IllegalStateException(this.getClass().getName() + " has no generateCode(..) method");
    }

    @Nullable
    public String getExitDescriptor() {
        return this.exitTypeDescriptor;
    }

    @Nullable
    protected final <T> T getValue(ExpressionState state, Class<T> desiredReturnType) throws EvaluationException {
        return ExpressionUtils.convertTypedValue(state.getEvaluationContext(), this.getValueInternal(state), desiredReturnType);
    }

    protected ValueRef getValueRef(ExpressionState state) throws EvaluationException, EvaluationException {
        throw new SpelEvaluationException(this.getStartPosition(), SpelMessage.NOT_ASSIGNABLE, new Object[]{this.toStringAST()});
    }

    public abstract TypedValue getValueInternal(ExpressionState var1) throws EvaluationException;

    protected static void generateCodeForArguments(MethodVisitor mv, CodeFlow cf, Member member, SpelNodeImpl[] arguments) {
        String[] paramDescriptors = null;
        boolean isVarargs = false;
        if (member instanceof Constructor) {
            Constructor<?> ctor = (Constructor)member;
            paramDescriptors = CodeFlow.toDescriptors(ctor.getParameterTypes());
            isVarargs = ctor.isVarArgs();
        } else {
            Method method = (Method)member;
            paramDescriptors = CodeFlow.toDescriptors(method.getParameterTypes());
            isVarargs = method.isVarArgs();
        }


        if (isVarargs) {
            int p = 0;
            int childCount = arguments.length;

            for(p = 0; p < paramDescriptors.length - 1; ++p) {
                generateCodeForArgument(mv, cf, arguments[p], paramDescriptors[p]);
            }

            SpelNodeImpl lastChild = childCount == 0 ? null : arguments[childCount - 1];
            String arrayType = paramDescriptors[paramDescriptors.length - 1];
            if (lastChild != null && arrayType.equals(lastChild.getExitDescriptor())) {
                generateCodeForArgument(mv, cf, lastChild, paramDescriptors[p]);
            } else {
                arrayType = arrayType.substring(1);
                CodeFlow.insertNewArrayCode(mv, childCount - p, arrayType);

                for(int var10 = 0; p < childCount; ++p) {
                    SpelNodeImpl child = arguments[p];
                    mv.visitInsn(89);
                    CodeFlow.insertOptimalLoad(mv, var10++);
                    generateCodeForArgument(mv, cf, child, arrayType);
                    CodeFlow.insertArrayStore(mv, arrayType);
                }
            }
        } else {
            for (int i = 0; i < paramDescriptors.length;i++) {
                generateCodeForArgument(mv, cf, arguments[i], paramDescriptors[i]);
            }
        }

    }

    protected static void generateCodeForArgument(MethodVisitor mv, CodeFlow cf, SpelNodeImpl argument, String paramDesc) {
        cf.enterCompilationScope();
        argument.generateCode(mv, cf);
        String lastDesc = cf.lastDescriptor();
        Assert.state(lastDesc != null, "No last descriptor");
        boolean primitiveOnStack = CodeFlow.isPrimitive(lastDesc);
        if (primitiveOnStack && paramDesc.charAt(0) == 'L') {
            CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
        } else if (paramDesc.length() == 1 && !primitiveOnStack) {
            CodeFlow.insertUnboxInsns(mv, paramDesc.charAt(0), lastDesc);
        } else if (!paramDesc.equals(lastDesc)) {
            CodeFlow.insertCheckCast(mv, paramDesc);
        }

        cf.exitCompilationScope();
    }
}
