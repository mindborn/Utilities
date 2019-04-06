/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.controllers;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author DELL
 */
public class Util
{

    public static void removeParentReferences(Object o) throws Exception
    {
        Set<Object> set = new HashSet<>();
        removeParentReferences(o, set);
    }

    public static void removeParentReferences(Object o, Set<Object> set) throws Exception
    {
        System.out.println("RPR:" + o.getClass());
        set.add(o);
        for (Method m : o.getClass().getDeclaredMethods())
        {
            final String methodname = m.getName();
            if (methodname.startsWith("get"))
            {
                Object o2 = m.invoke(o);
                System.out.println(methodname + ":" + o2);
                if (o2 instanceof Collection)
                {
                    for (Object o3 : ((Collection) o2))
                    {
                        removeParentReferences(o3, set);
                    }
                }
                if (set.contains(o2))
                {
                    final String setmethod = "s" + methodname.substring(1);
                    System.out.println("setmethod = " + setmethod);
                    Method m2 = findMethod(o.getClass(), setmethod);
                    System.out.println("m2 = " + m2);
                    if (m2 != null)
                    {
                        m2.invoke(o, new Object[]
                        {
                            null
                        });
                        System.out.println(m.invoke(o));
                    }
                }
            }
        }
    }

    public static Method findMethod(Class c, String name)
    {
        System.out.println("FM:" + c + ":" + name);
        for (Method m : c.getDeclaredMethods())
        {
            System.out.println("m.getName() = " + m.getName());
            if (m.getName().equals(name))
            {
                return m;
            }
        }
        return null;
    }
}
