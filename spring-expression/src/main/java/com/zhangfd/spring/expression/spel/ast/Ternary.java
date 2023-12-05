//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.zhangfd.spring.expression.spel.ast;


import com.zhangfd.spring.asm.Label;
import com.zhangfd.spring.asm.MethodVisitor;
import com.zhangfd.spring.expression.EvaluationException;
import com.zhangfd.spring.expression.TypedValue;
import com.zhangfd.spring.expression.spel.CodeFlow;
import com.zhangfd.spring.expression.spel.ExpressionState;
import com.zhangfd.spring.expression.spel.SpelEvaluationException;
import com.zhangfd.spring.expression.spel.SpelMessage;
import com.zhangfd.spring.util.Assert;
import com.zhangfd.spring.util.ObjectUtils;

public class Ternary extends SpelNodeImpl {
    public Ternary(int startPos, int endPos, SpelNodeImpl... args) {
        super(startPos, endPos, args);
    }

    public TypedValue getValueInternal(ExpressionState state) throws EvaluationException {
        Boolean value = (Boolean)this.children[0].getValue(state, Boolean.class);
        if (value == null) {
            throw new SpelEvaluationException(this.getChild(0).getStartPosition(), SpelMessage.TYPE_CONVERSION_ERROR, new Object[]{"null", "boolean"});
        } else {
            TypedValue result = this.children[value ? 1 : 2].getValueInternal(state);
            this.computeExitTypeDescriptor();
            return result;
        }
    }

    public String toStringAST() {
        return this.getChild(0).toStringAST() + " ? " + this.getChild(1).toStringAST() + " : " + this.getChild(2).toStringAST();
    }

    private void computeExitTypeDescriptor() {
        if (this.exitTypeDescriptor == null && this.children[1].exitTypeDescriptor != null && this.children[2].exitTypeDescriptor != null) {
            String leftDescriptor = this.children[1].exitTypeDescriptor;
            String rightDescriptor = this.children[2].exitTypeDescriptor;
            if (ObjectUtils.nullSafeEquals(leftDescriptor, rightDescriptor)) {
                this.exitTypeDescriptor = leftDescriptor;
            } else {
                this.exitTypeDescriptor = "Ljava/lang/Object";
            }
        }

    }

    public boolean isCompilable() {
        SpelNodeImpl condition = this.children[0];
        SpelNodeImpl left = this.children[1];
        SpelNodeImpl right = this.children[2];
        return condition.isCompilable() && left.isCompilable() && right.isCompilable() && CodeFlow.isBooleanCompatible(condition.exitTypeDescriptor) && left.exitTypeDescriptor != null && right.exitTypeDescriptor != null;
    }

    public void generateCode(MethodVisitor mv, CodeFlow cf) {
        this.computeExitTypeDescriptor();
        cf.enterCompilationScope();
        this.children[0].generateCode(mv, cf);
        String lastDesc = cf.lastDescriptor();
        Assert.state(lastDesc != null, "No last descriptor");
        if (!CodeFlow.isPrimitive(lastDesc)) {
            CodeFlow.insertUnboxInsns(mv, 'Z', lastDesc);
        }

        cf.exitCompilationScope();
        Label elseTarget = new Label();
        Label endOfIf = new Label();
        mv.visitJumpInsn(153, elseTarget);
        cf.enterCompilationScope();
        this.children[1].generateCode(mv, cf);
        if (!CodeFlow.isPrimitive(this.exitTypeDescriptor)) {
            lastDesc = cf.lastDescriptor();
            Assert.state(lastDesc != null, "No last descriptor");
            CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
        }

        cf.exitCompilationScope();
        mv.visitJumpInsn(167, endOfIf);
        mv.visitLabel(elseTarget);
        cf.enterCompilationScope();
        this.children[2].generateCode(mv, cf);
        if (!CodeFlow.isPrimitive(this.exitTypeDescriptor)) {
            lastDesc = cf.lastDescriptor();
            Assert.state(lastDesc != null, "No last descriptor");
            CodeFlow.insertBoxIfNecessary(mv, lastDesc.charAt(0));
        }

        cf.exitCompilationScope();
        mv.visitLabel(endOfIf);
        cf.pushDescriptor(this.exitTypeDescriptor);
    }
}
