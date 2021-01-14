/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonutils;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Manoj
 */

public class JsonUtils
{

    public static String serialize(Object o)
    {
        try
        {
            return serialize(o, new HashSet<>());
        } catch (Exception ex)
        {
            Logger.getLogger(JsonUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    private static String serialize(Object o, HashSet<Object> set) throws Exception
    {
        if (set.contains(o))
        {
            return "{}";
        }

        if (o instanceof String)
        {
            return "\"" + o + "\"";
        }
        if (o instanceof Number)
        {
            return "" + o;
        }
        if (o instanceof Array)
        {
            StringBuilder sb = new StringBuilder();
//            Array arr=(Array)o;
            sb.append("[");
            int length = Array.getLength(o);
            for (int i = 0; i < length; i++)
            {
                if (i > 0)
                {
                    sb.append(",");
                }
                Object o2 = Array.get(o, i);
                sb.append(serialize(o2, set));
            }
            sb.append("]");
            return sb.toString();
        }
        if (o instanceof Collection)
        {
            StringBuilder sb = new StringBuilder();
            Collection c = (Collection) o;
            sb.append("[");
//            int length = Array.getLength(o);
            boolean first = true;
            for (Object o2 : c)
            {
                if (!first)
                {
                    sb.append(",");
                } else
                {
                    first = false;
                }
                sb.append(serialize(o2, set));
            }
            sb.append("]");
            return sb.toString();
        }
        if (o instanceof Map)
        {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            Map<?, ?> map = (Map<?, ?>) o;

            for (Map.Entry e : map.entrySet())
            {
                sb.append(serialize(e.getKey(), set));
                sb.append(":");
                sb.append(serialize(e.getValue(), set));
            }
            sb.append("}");
            return sb.toString();
        }

        //POJO
        set.add(o);

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Method[] methods = o.getClass().getDeclaredMethods();

        boolean first = true;
        for (Method m : methods)
        {
            String methodname = m.getName();
            if (methodname.startsWith("get"))
            {
                System.out.println(o.getClass()+":"+methodname);
                Object o2 = m.invoke(o, (Object[]) null);

                String name = methodname.substring(3);
                if (first)
                {
                    first = false;
                } else
                {
                    sb.append(",");
                }
                sb.append(name).append(":").append(serialize(o2, set));
            }
        }
        sb.append("}");
        return sb.toString();
    }

}
