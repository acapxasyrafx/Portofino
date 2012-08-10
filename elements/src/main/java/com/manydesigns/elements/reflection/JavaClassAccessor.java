/*
 * Copyright (C) 2005-2012 ManyDesigns srl.  All rights reserved.
 * http://www.manydesigns.com/
 *
 * Unless you have purchased a commercial license agreement from ManyDesigns srl,
 * the following license terms apply:
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as published by
 * the Free Software Foundation.
 *
 * There are special exceptions to the terms and conditions of the GPL
 * as it is applied to this software. View the full text of the
 * exception in file OPEN-SOURCE-LICENSE.txt in the directory of this
 * software distribution.
 *
 * This program is distributed WITHOUT ANY WARRANTY; and without the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see http://www.gnu.org/licenses/gpl.txt
 * or write to:
 * Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307  USA
 *
 */

package com.manydesigns.elements.reflection;

import com.manydesigns.elements.util.ReflectionUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* @author Paolo Predonzani     - paolo.predonzani@manydesigns.com
* @author Angelo Lupo          - angelo.lupo@manydesigns.com
* @author Giampiero Granatella - giampiero.granatella@manydesigns.com
* @author Alessio Stalla       - alessio.stalla@manydesigns.com
*/
public class JavaClassAccessor implements ClassAccessor {
    public static final String copyright =
            "Copyright (c) 2005-2012, ManyDesigns srl";

    //**************************************************************************
    // Fields
    //**************************************************************************

    protected final Class javaClass;
    protected final PropertyAccessor[] propertyAccessors;

    //**************************************************************************
    // Static fields and methods
    //**************************************************************************

    protected static final Map<Class, JavaClassAccessor> classAccessorCache;
    public static final Logger logger =
                LoggerFactory.getLogger(JavaClassAccessor.class);

    static {
        classAccessorCache = new HashMap<Class, JavaClassAccessor>();
    }

    public static JavaClassAccessor getClassAccessor(Class javaClass) {
        JavaClassAccessor cachedResult = classAccessorCache.get(javaClass);
        if (cachedResult == null) {
            logger.debug("Cache miss for: {}", javaClass);
            cachedResult = new JavaClassAccessor(javaClass);
            logger.debug("Caching key: {} - Value: {}",
                    javaClass, cachedResult);
            classAccessorCache.put(javaClass, cachedResult);
        } else {
            logger.debug("Cache hit for: {} - Value: {}",
                    javaClass, cachedResult);
        }
        return cachedResult;
    }


    //**************************************************************************
    // Constructors and initialization
    //**************************************************************************

    protected JavaClassAccessor(Class javaClass) {
        this.javaClass = javaClass;

        List<PropertyAccessor> accessorList =
                new ArrayList<PropertyAccessor>();

        // handle properties through introspection
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(javaClass);
            PropertyDescriptor[] propertyDescriptors =
                    beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor current : propertyDescriptors) {
                accessorList.add(new JavaPropertyAccessor(current));
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }

        // handle public fields
        for (Field field : javaClass.getFields()) {
            if (isPropertyPresent(accessorList, field.getName())) {
                continue;
            }
            accessorList.add(new JavaFieldAccessor(field));
        }
        propertyAccessors = new PropertyAccessor[accessorList.size()];
        accessorList.toArray(propertyAccessors);
    }

    private boolean isPropertyPresent(List<PropertyAccessor> accessorList,
                                      String name) {
        for (PropertyAccessor current : accessorList) {
            if (current.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    
    //**************************************************************************
    // ClassAccessor implementation
    //**************************************************************************

    public String getName() {
        return javaClass.getName();
    }

    public PropertyAccessor getProperty(String propertyName)
            throws NoSuchFieldException {
        for (PropertyAccessor current : propertyAccessors) {
            if (current.getName().equals(propertyName)) {
                return current;
            }
        }
        throw new NoSuchFieldException(propertyName);
    }

    public PropertyAccessor[] getProperties() {
        return propertyAccessors.clone();
    }

    public PropertyAccessor[] getKeyProperties() {
        return new PropertyAccessor[0];
    }

    public Object newInstance() {
        return ReflectionUtil.newInstance(javaClass);
    }


    //**************************************************************************
    // AnnotatedElement implementation
    //**************************************************************************

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return javaClass.isAnnotationPresent(annotationClass);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        //noinspection unchecked
        return (T) javaClass.getAnnotation(annotationClass);
    }

    public Annotation[] getAnnotations() {
        return javaClass.getAnnotations();
    }

    public Annotation[] getDeclaredAnnotations() {
        return javaClass.getDeclaredAnnotations();
    }

    //**************************************************************************
    // Getters
    //**************************************************************************

    public Class getJavaClass() {
        return javaClass;
    }


    //**************************************************************************
    // Other methods
    //**************************************************************************

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("javaClass", javaClass)
                .toString();
    }
}
