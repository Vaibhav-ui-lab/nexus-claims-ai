pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "nexus-claims-ai"

include("backend:common-contracts")
include("backend:api-gateway")
include("backend:claim-intake-service")
include("backend:claims-orchestrator-service")
include("backend:ai-fraud-triage-service")
include("backend:billing-payment-service")
