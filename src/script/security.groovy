import hudson.model.User
import hudson.security.GlobalMatrixAuthorizationStrategy
import hudson.security.HudsonPrivateSecurityRealm
import hudson.security.PermissionGroup
import hudson.security.SecurityRealm
import jenkins.model.Jenkins

def jenkins = Jenkins.instance

if (jenkins.securityRealm != SecurityRealm.NO_AUTHENTICATION) {
    return
}

// Enable security.
// Jenkins’ own user database.
// Don't allow users to sign up.
def realm = new HudsonPrivateSecurityRealm(false)
jenkins.securityRealm = realm

// Matrix-based security
def authorizationStrategy = new GlobalMatrixAuthorizationStrategy()
// Grant all permissions to authenticated users.
// Anonymous can do nothing.
PermissionGroup.all.each { group ->
    group.permissions.each {
        authorizationStrategy.add(it, 'authenticated')
    }
}
jenkins.authorizationStrategy = authorizationStrategy

// Create an administrator account.
if (User.all.size() == 0) {
    def adminUsername = System.getenv('JENKINS_ADMIN_USERNAME') ?: 'admin'
    def adminPassword = System.getenv('JENKINS_ADMIN_PASSWORD') ?: 'secret'
    println 'Create an administrator account.'
    realm.createAccount(adminUsername, adminPassword)
}
