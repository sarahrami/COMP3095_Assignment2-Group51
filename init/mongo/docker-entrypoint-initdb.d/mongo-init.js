db = db.getSiblingDB("spring-social");

db.createUser(
    {
        user: "rootadmin",
        pwd: "password",
        roles: [{role: "readWrite", db: "spring-social"}]
    }
)

db.createCollection("user");