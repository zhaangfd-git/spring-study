import com.zhangfd.spring.core.ResolvableType;

import java.util.HashMap;
import java.util.List;

public class ResolvableTypeTest {
    //ResolvableType对应java的Type类型，比Type使用更方便，功能也更强大。
    public static void main(String[] args) throws Exception {
        test2();
    }

    /**
     *   @ ResolvableType 类学习:
     */
    public static   void   test2() throws  Exception{
       /* Field param = GenericClazz.class.getDeclaredField("param");
        Type genericType = param.getGenericType();
        ParameterizedType type = (ParameterizedType) genericType;
        Type[] typeArguments = type.getActualTypeArguments();
        System.out.println("从 HashMap<String, List<Integer>> 中获取 String:" + typeArguments[0]);
        System.out.println("从 HashMap<String, List<Integer>> 中获取 List<Integer> :" + typeArguments[1]);
        System.out.println(
                "从 HashMap<String, List<Integer>> 中获取 List :" + ((ParameterizedType) typeArguments[1]).getRawType());
        System.out.println("从 HashMap<String, List<Integer>> 中获取 Integer:" + ((ParameterizedType) typeArguments[1])
                .getActualTypeArguments()[0]);
        System.out.println("从 HashMap<String, List<Integer>> 中获取父类型:"+param.getType().getGenericSuperclass());*/

        ResolvableType param = ResolvableType.forField(GenericClazz.class.getDeclaredField("param"));
        System.out.println("从 HashMap<String, List<Integer>> 中获取 String:" + param.getGeneric(0).resolve());
        System.out.println("从 HashMap<String, List<Integer>> 中获取 List<Integer> :" + param.getGeneric(1));
        System.out.println(
                "从 HashMap<String, List<Integer>> 中获取 List :" + param.getGeneric(1).resolve());
        System.out.println("从 HashMap<String, List<Integer>> 中获取 Integer:" + param.getGeneric(1,0));
        System.out.println("从 HashMap<String, List<Integer>> 中获取父类型:" +param.getSuperType());


    }
    public class GenericClazz {

        private HashMap<String, List<Integer>> param;

    }
}
