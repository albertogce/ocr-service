import org.sonatype.nexus.security.user.UserManager
import org.sonatype.nexus.security.role.RoleIdentifier

def securitySystem = container.lookup(org.sonatype.nexus.security.SecuritySystem.class.name)

def userId = "admin"
def realm = UserManager.DEFAULT_SOURCE

def user = securitySystem.getUser(userId)
log.info("Usuario encontrado: ${user.getUserId()}")

def newRoles = user.getRoles() + new RoleIdentifier("default", "nx-admin") // ya la tiene, pero por seguridad

// O agregar directamente permisos con un rol temporal
def roleManager = container.lookup(org.sonatype.nexus.security.role.RoleManager.class.name)
def existing = roleManager.listRoles().find { it.roleId == "deploy-role" }

if (existing == null) {
    roleManager.addRole(new org.sonatype.nexus.security.role.Role(
        roleId: "deploy-role",
        source: "default",
        name: "Custom Maven Deploy Role",
        description: "Allows deploy to maven-releases",
        privileges: ["nx-repository-view-maven2-maven-releases-add", "nx-repository-view-maven2-maven-releases-edit"],
        roles: []
    ))
    log.info("Rol 'deploy-role' creado")
}

if (!user.getRoles().any { it.roleId == "deploy-role" }) {
    user.getRoles().add(new RoleIdentifier("default", "deploy-role"))
    securitySystem.updateUser(user)
    log.info("Rol 'deploy-role' asignado al usuario 'admin'")
}
