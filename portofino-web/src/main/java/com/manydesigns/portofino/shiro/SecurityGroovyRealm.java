/*
 * Copyright (C) 2005-2013 ManyDesigns srl.  All rights reserved.
 * http://www.manydesigns.com/
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.manydesigns.portofino.shiro;

import com.manydesigns.elements.ElementsThreadLocals;
import com.manydesigns.portofino.di.Injections;
import groovy.util.GroovyScriptEngine;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.LifecycleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Paolo Predonzani     - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo          - angelo.lupo@manydesigns.com
 * @author Giampiero Granatella - giampiero.granatella@manydesigns.com
 * @author Alessio Stalla       - alessio.stalla@manydesigns.com
 */
public class SecurityGroovyRealm implements PortofinoRealm {
    public static final String copyright =
            "Copyright (c) 2005-2013, ManyDesigns srl";

    //--------------------------------------------------------------------------
    // Logger
    //--------------------------------------------------------------------------

    public static final Logger logger = LoggerFactory.getLogger(SecurityGroovyRealm.class);

    //--------------------------------------------------------------------------
    // Properties
    //--------------------------------------------------------------------------

    protected final File classpath;
    protected final GroovyScriptEngine scriptEngine;
    protected PortofinoRealm security;

    protected CacheManager cacheManager;

    //--------------------------------------------------------------------------
    // Constructors
    //--------------------------------------------------------------------------

    public SecurityGroovyRealm(GroovyScriptEngine scriptEngine, File classpath) {
        this.scriptEngine = scriptEngine;
        this.classpath = classpath;
    }

    //--------------------------------------------------------------------------
    // Delegation support
    //--------------------------------------------------------------------------

    private PortofinoRealm ensureDelegate() {
        try {
            File scriptFile = new File(classpath, "Security.groovy");
            Class<?> scriptClass = scriptEngine.loadScriptByName(scriptFile.toURI().toString());
            Object security = this.security;
            if(!scriptClass.isInstance(security)) try {
                logger.info("Refreshing Portofino Realm Delegate instance (Security.groovy)");
                security = scriptClass.newInstance();
            } catch (Exception e) {
                throw new Error("Couldn't load security script", e);
            }
            if(security instanceof PortofinoRealm) {
                PortofinoRealm realm = (PortofinoRealm) security;
                configureDelegate(realm);
                this.security = realm;
                return realm;
            } else {
                 throw new Error("Security object is not an instance of " + PortofinoRealm.class + ": " + security);
            }
        } catch (Exception e) {
            throw new Error("Security.groovy not found or not loadable", e);
        }
    }

    protected void configureDelegate(PortofinoRealm security) {
        Injections.inject(
                security,
                ElementsThreadLocals.getServletContext(),
                ElementsThreadLocals.getHttpServletRequest());
        security.setCacheManager(cacheManager);
        LifecycleUtils.init(security);
    }

    //--------------------------------------------------------------------------
    // PortofinoRealm implementation
    //--------------------------------------------------------------------------
    
    @Override
    public void verifyUser(Serializable user) {
        ensureDelegate().verifyUser(user);
    }

    @Override
    public void changePassword(Serializable user, String newPassword) {
        ensureDelegate().changePassword(user, newPassword);
    }

    @Override
    public String generateOneTimeToken(Serializable user) {
        return ensureDelegate().generateOneTimeToken(user);
    }

    @Override
    public Set<String> getUsers() {
        return ensureDelegate().getUsers();
    }

    @Override
    public Set<String> getGroups() {
        return ensureDelegate().getGroups();
    }

    @Override
    public String getAllGroup() {
        return ensureDelegate().getAllGroup();
    }

    @Override
    public String getAnonymousGroup() {
        return ensureDelegate().getAnonymousGroup();
    }

    @Override
    public String getRegisteredGroup() {
        return ensureDelegate().getRegisteredGroup();
    }

    @Override
    public String getAdministratorsGroup() {
        return ensureDelegate().getAdministratorsGroup();
    }

    //--------------------------------------------------------------------------
    // Realm implementation
    //--------------------------------------------------------------------------

    @Override
    public String getName() {
        return ensureDelegate().getName();
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return ensureDelegate().supports(token);
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        return ensureDelegate().getAuthenticationInfo(token);
    }

    //--------------------------------------------------------------------------
    // Authorizer implementation
    //--------------------------------------------------------------------------

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        return ensureDelegate().isPermitted(principals, permission);
    }

    @Override
    public boolean isPermitted(PrincipalCollection subjectPrincipal, Permission permission) {
        return ensureDelegate().isPermitted(subjectPrincipal, permission);
    }

    @Override
    public boolean[] isPermitted(PrincipalCollection subjectPrincipal, String... permissions) {
        return ensureDelegate().isPermitted(subjectPrincipal, permissions);
    }

    @Override
    public boolean[] isPermitted(PrincipalCollection subjectPrincipal, List<Permission> permissions) {
        return ensureDelegate().isPermitted(subjectPrincipal, permissions);
    }

    @Override
    public boolean isPermittedAll(PrincipalCollection subjectPrincipal, String... permissions) {
        return ensureDelegate().isPermittedAll(subjectPrincipal, permissions);
    }

    @Override
    public boolean isPermittedAll(PrincipalCollection subjectPrincipal, Collection<Permission> permissions) {
        return ensureDelegate().isPermittedAll(subjectPrincipal, permissions);
    }

    @Override
    public void checkPermission(PrincipalCollection subjectPrincipal, String permission) throws AuthorizationException {
        ensureDelegate().checkPermission(subjectPrincipal, permission);
    }

    @Override
    public void checkPermission(PrincipalCollection subjectPrincipal, Permission permission) throws AuthorizationException {
        ensureDelegate().checkPermission(subjectPrincipal, permission);
    }

    @Override
    public void checkPermissions(PrincipalCollection subjectPrincipal, String... permissions) throws AuthorizationException {
        ensureDelegate().checkPermissions(subjectPrincipal, permissions);
    }

    @Override
    public void checkPermissions(PrincipalCollection subjectPrincipal, Collection<Permission> permissions) throws AuthorizationException {
        ensureDelegate().checkPermissions(subjectPrincipal, permissions);
    }

    @Override
    public boolean hasRole(PrincipalCollection subjectPrincipal, String roleIdentifier) {
        return ensureDelegate().hasRole(subjectPrincipal, roleIdentifier);
    }

    @Override
    public boolean[] hasRoles(PrincipalCollection subjectPrincipal, List<String> roleIdentifiers) {
        return ensureDelegate().hasRoles(subjectPrincipal, roleIdentifiers);
    }

    @Override
    public boolean hasAllRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers) {
        return ensureDelegate().hasAllRoles(subjectPrincipal, roleIdentifiers);
    }

    @Override
    public void checkRole(PrincipalCollection subjectPrincipal, String roleIdentifier) throws AuthorizationException {
        ensureDelegate().checkRole(subjectPrincipal, roleIdentifier);
    }

    @Override
    public void checkRoles(PrincipalCollection subjectPrincipal, Collection<String> roleIdentifiers) throws AuthorizationException {
        ensureDelegate().checkRoles(subjectPrincipal, roleIdentifiers);
    }

    @Override
    public void checkRoles(PrincipalCollection subjectPrincipal, String... roleIdentifiers) throws AuthorizationException {
        ensureDelegate().checkRoles(subjectPrincipal, roleIdentifiers);
    }

    @Override
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        ensureDelegate().setCacheManager(cacheManager);
    }
}
