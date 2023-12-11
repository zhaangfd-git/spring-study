package com.zhangfd.spring.context.test;

import com.zhangfd.spring.beans.support.ResourceEditorRegistrar;
import com.zhangfd.spring.core.env.StandardEnvironment;
import com.zhangfd.spring.core.io.support.PathMatchingResourcePatternResolver;

/**
 * @author: zhangfd
 * @date: 2023/12/11 23:03
 * @version: 1.0
 * @describe:
 */
public class ResourceEditorRegistrarTest {

    public static void main(String[] args) {
        ResourceEditorRegistrar registrar = new ResourceEditorRegistrar(new PathMatchingResourcePatternResolver(Thread.currentThread().getContextClassLoader()),new StandardEnvironment());

        //registrar.registerCustomEditors();
    }

}
