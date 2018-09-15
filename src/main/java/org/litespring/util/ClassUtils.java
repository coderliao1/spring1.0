package org.litespring.util;

import java.lang.reflect.Proxy;
import java.util.*;

public abstract class ClassUtils {
    /** The package separator character: '.' */

    private static final char PACKAGE_SEPARATOR = '.';



    /** The path separator character: '/' */

    private static final char PATH_SEPARATOR = '/';



    /** The inner class separator character: '$' */

    private static final char INNER_CLASS_SEPARATOR = '$';



    /** The CGLIB class separator: "$$" */

    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    /**

     * Map with primitive wrapper type as key and corresponding primitive

     * type as value, for example: Integer.class -> int.class.

     */

    private static final Map<Class<?>, Class<?>> wrapperToPrimitiveTypeMap = new HashMap<Class<?>, Class<?>>(8);



    /**

     * Map with primitive type as key and corresponding wrapper

     * type as value, for example: int.class -> Integer.class.

     */

    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new HashMap<Class<?>, Class<?>>(8);



    static {

        wrapperToPrimitiveTypeMap.put(Boolean.class, boolean.class);

        wrapperToPrimitiveTypeMap.put(Byte.class, byte.class);

        wrapperToPrimitiveTypeMap.put(Character.class, char.class);

        wrapperToPrimitiveTypeMap.put(Double.class, double.class);

        wrapperToPrimitiveTypeMap.put(Float.class, float.class);

        wrapperToPrimitiveTypeMap.put(Integer.class, int.class);

        wrapperToPrimitiveTypeMap.put(Long.class, long.class);

        wrapperToPrimitiveTypeMap.put(Short.class, short.class);



        for (Map.Entry<Class<?>, Class<?>> entry : wrapperToPrimitiveTypeMap.entrySet()) {

            primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());



        }



    }





    public static ClassLoader getDefaultClassLoader() {

        ClassLoader cl = null;

        try {

            cl = Thread.currentThread().getContextClassLoader();

        }

        catch (Throwable ex) {

            // Cannot access thread context ClassLoader - falling back...

        }

        if (cl == null) {

            // No thread context class loader -> use class loader of this class.

            cl = ClassUtils.class.getClassLoader();

            if (cl == null) {

                // getClassLoader() returning null indicates the bootstrap ClassLoader

                try {

                    cl = ClassLoader.getSystemClassLoader();

                }

                catch (Throwable ex) {

                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...

                }

            }

        }

        return cl;

    }

    public static boolean isAssignableValue(Class<?> type, Object value) {

        Assert.notNull(type, "Type must not be null");

        return (value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive());

    }

    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {

        Assert.notNull(lhsType, "Left-hand side type must not be null");

        Assert.notNull(rhsType, "Right-hand side type must not be null");

        if (lhsType.isAssignableFrom(rhsType)) {

            return true;

        }

        if (lhsType.isPrimitive()) {

            Class<?> resolvedPrimitive = wrapperToPrimitiveTypeMap.get(rhsType);

            if (resolvedPrimitive != null && lhsType.equals(resolvedPrimitive)) {

                return true;

            }

        }

        else {

            Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(rhsType);

            if (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper)) {

                return true;

            }

        }

        return false;

    }
    public static String convertResourcePathToClassName(String resourcePath) {

        Assert.notNull(resourcePath, "Resource path must not be null");

        return resourcePath.replace(PATH_SEPARATOR, PACKAGE_SEPARATOR);

    }

    public static String convertClassNameToResourcePath(String className) {

        Assert.notNull(className, "Class name must not be null");

        return className.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);

    }

    public static String getShortName(String className) {

        int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);

        int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);

        if (nameEndIndex == -1) {

            nameEndIndex = className.length();

        }

        String shortName = className.substring(lastDotIndex + 1, nameEndIndex);

        shortName = shortName.replace(INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);

        return shortName;

    }
    /**

     * Return a path suitable for use with {@code ClassLoader.getResource}

     * (also suitable for use with {@code Class.getResource} by prepending a

     * slash ('/') to the return value). Built by taking the package of the specified

     * class file, converting all dots ('.') to slashes ('/'), adding a trailing slash

     * if necessary, and concatenating the specified resource name to this.

     * <br/>As such, this function may be used to build a path suitable for

     * loading a resource file that is in the same package as a class file,

     * although {@link org.springframework.core.io.ClassPathResource} is usually

     * even more convenient.

     * @param clazz the Class whose package will be used as the base

     * @param resourceName the resource name to append. A leading slash is optional.

     * @return the built-up resource path

     * @see ClassLoader#getResource

     * @see Class#getResource

     */

    public static String addResourcePathToPackagePath(Class<?> clazz, String resourceName) {

        Assert.notNull(resourceName, "Resource name must not be null");

        if (!resourceName.startsWith("/")) {

            return classPackageAsResourcePath(clazz) + "/" + resourceName;

        }

        return classPackageAsResourcePath(clazz) + resourceName;

    }



    /**

     * Given an input class object, return a string which consists of the

     * class's package name as a pathname, i.e., all dots ('.') are replaced by

     * slashes ('/'). Neither a leading nor trailing slash is added. The result

     * could be concatenated with a slash and the name of a resource and fed

     * directly to {@code ClassLoader.getResource()}. For it to be fed to

     * {@code Class.getResource} instead, a leading slash would also have

     * to be prepended to the returned value.

     * @param clazz the input class. A {@code null} value or the default

     * (empty) package will result in an empty string ("") being returned.

     * @return a path which represents the package name

     * @see ClassLoader#getResource

     * @see Class#getResource

     */

    public static String classPackageAsResourcePath(Class<?> clazz) {

        if (clazz == null) {

            return "";

        }

        String className = clazz.getName();

        int packageEndIndex = className.lastIndexOf(PACKAGE_SEPARATOR);

        if (packageEndIndex == -1) {

            return "";

        }

        String packageName = className.substring(0, packageEndIndex);

        return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);

    }









    /**

     * Copy the given Collection into a Class array.

     * The Collection must contain Class elements only.

     * @param collection the Collection to copy

     * @return the Class array ({@code null} if the passed-in

     * Collection was {@code null})

     */

    public static Class<?>[] toClassArray(Collection<Class<?>> collection) {

        if (collection == null) {

            return null;

        }

        return collection.toArray(new Class<?>[collection.size()]);

    }



    /**

     * Return all interfaces that the given instance implements as array,

     * including ones implemented by superclasses.

     * @param instance the instance to analyze for interfaces

     * @return all interfaces that the given instance implements as array

     */

    public static Class<?>[] getAllInterfaces(Object instance) {

        Assert.notNull(instance, "Instance must not be null");

        return getAllInterfacesForClass(instance.getClass());

    }



    /**

     * Return all interfaces that the given class implements as array,

     * including ones implemented by superclasses.

     * <p>If the class itself is an interface, it gets returned as sole interface.

     * @param clazz the class to analyze for interfaces

     * @return all interfaces that the given object implements as array

     */

    public static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {

        return getAllInterfacesForClass(clazz, null);

    }



    /**

     * Return all interfaces that the given class implements as array,

     * including ones implemented by superclasses.

     * <p>If the class itself is an interface, it gets returned as sole interface.

     * @param clazz the class to analyze for interfaces

     * @param classLoader the ClassLoader that the interfaces need to be visible in

     * (may be {@code null} when accepting all declared interfaces)

     * @return all interfaces that the given object implements as array

     */

    public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, ClassLoader classLoader) {

        Set<Class> ifcs = getAllInterfacesForClassAsSet(clazz, classLoader);

        return ifcs.toArray(new Class[ifcs.size()]);

    }



    /**

     * Return all interfaces that the given instance implements as Set,

     * including ones implemented by superclasses.

     * @param instance the instance to analyze for interfaces

     * @return all interfaces that the given instance implements as Set

     */

    public static Set<Class> getAllInterfacesAsSet(Object instance) {

        Assert.notNull(instance, "Instance must not be null");

        return getAllInterfacesForClassAsSet(instance.getClass());

    }



    /**

     * Return all interfaces that the given class implements as Set,

     * including ones implemented by superclasses.

     * <p>If the class itself is an interface, it gets returned as sole interface.

     * @param clazz the class to analyze for interfaces

     * @return all interfaces that the given object implements as Set

     */

    public static Set<Class> getAllInterfacesForClassAsSet(Class clazz) {

        return getAllInterfacesForClassAsSet(clazz, null);

    }



    /**

     * Return all interfaces that the given class implements as Set,

     * including ones implemented by superclasses.

     * <p>If the class itself is an interface, it gets returned as sole interface.

     * @param clazz the class to analyze for interfaces

     * @param classLoader the ClassLoader that the interfaces need to be visible in

     * (may be {@code null} when accepting all declared interfaces)

     * @return all interfaces that the given object implements as Set

     */

    public static Set<Class> getAllInterfacesForClassAsSet(Class clazz, ClassLoader classLoader) {

        Assert.notNull(clazz, "Class must not be null");

        if (clazz.isInterface() && isVisible(clazz, classLoader)) {

            return Collections.singleton(clazz);

        }

        Set<Class> interfaces = new LinkedHashSet<Class>();

        while (clazz != null) {

            Class<?>[] ifcs = clazz.getInterfaces();

            for (Class<?> ifc : ifcs) {

                interfaces.addAll(getAllInterfacesForClassAsSet(ifc, classLoader));

            }

            clazz = clazz.getSuperclass();

        }

        return interfaces;

    }



    /**

     * Create a composite interface Class for the given interfaces,

     * implementing the given interfaces in one single Class.

     * <p>This implementation builds a JDK proxy class for the given interfaces.

     * @param interfaces the interfaces to merge

     * @param classLoader the ClassLoader to create the composite Class in

     * @return the merged interface as Class

     * @see java.lang.reflect.Proxy#getProxyClass

     */

    public static Class<?> createCompositeInterface(Class<?>[] interfaces, ClassLoader classLoader) {



        return Proxy.getProxyClass(classLoader, interfaces);

    }



    /**

     * Determine the common ancestor of the given classes, if any.

     * @param clazz1 the class to introspect

     * @param clazz2 the other class to introspect

     * @return the common ancestor (i.e. common superclass, one interface

     * extending the other), or {@code null} if none found. If any of the

     * given classes is {@code null}, the other class will be returned.

     * @since 3.2.6

     */

    public static Class<?> determineCommonAncestor(Class<?> clazz1, Class<?> clazz2) {

        if (clazz1 == null) {

            return clazz2;

        }

        if (clazz2 == null) {

            return clazz1;

        }

        if (clazz1.isAssignableFrom(clazz2)) {

            return clazz1;

        }

        if (clazz2.isAssignableFrom(clazz1)) {

            return clazz2;

        }

        Class<?> ancestor = clazz1;

        do {

            ancestor = ancestor.getSuperclass();

            if (ancestor == null || Object.class.equals(ancestor)) {

                return null;

            }

        }

        while (!ancestor.isAssignableFrom(clazz2));

        return ancestor;

    }



    /**

     * Check whether the given class is visible in the given ClassLoader.

     * @param clazz the class to check (typically an interface)

     * @param classLoader the ClassLoader to check against (may be {@code null},

     * in which case this method will always return {@code true})

     */

    public static boolean isVisible(Class<?> clazz, ClassLoader classLoader) {

        if (classLoader == null) {

            return true;

        }

        try {

            Class<?> actualClass = classLoader.loadClass(clazz.getName());

            return (clazz == actualClass);

            // Else: different interface class found...

        }

        catch (ClassNotFoundException ex) {

            // No interface class found...

            return false;

        }

    }



    /**

     * Check whether the given object is a CGLIB proxy.

     * @param object the object to check

     * @see org.springframework.aop.support.AopUtils#isCglibProxy(Object)

     */

    public static boolean isCglibProxy(Object object) {

        return isCglibProxyClass(object.getClass());

    }



    /**

     * Check whether the specified class is a CGLIB-generated class.

     * @param clazz the class to check

     */

    public static boolean isCglibProxyClass(Class<?> clazz) {

        return (clazz != null && isCglibProxyClassName(clazz.getName()));

    }



    /**

     * Check whether the specified class name is a CGLIB-generated class.

     * @param className the class name to check

     */

    public static boolean isCglibProxyClassName(String className) {

        return (className != null && className.contains(CGLIB_CLASS_SEPARATOR));

    }



}

