# Bulding and deploying in Docker

## Dependency caching and publishing

Many of services, presented in this repository,
have an API package which is a dependency for other services.
Originally, resolving this dependency meant bulding the package, deploying it into local Maven
and then referencing to local Maven in other services.
Unfortunatelly, in order to build the software in a predictable environment,
this method has been abandoned.
That's because local Maven can be supported in Docker only by using a volume
but it is expected that each service is built as a separate Docker image.
Thus, since at built time, feature of volumes is unavailable in Docker,
there has to be an external repository to do the job.

After a little research, Artifactory project has been choosen to resolve issue with publishing of API artifacts.
Also, Artifactory provides two features that seems quite useful: ability to proxy remote repositories,
including caching of requested data, and feature of virtual repositories that represent set of repositories as one.

Thus, to build this project, it is required to raise Artifactory service in Docker first:
```
docker-compose -f docker-compose-storage.yml up
```

Then, after Artifactory is up, it is necessary to deploy first-time configuration by calling:
```
docker-compose -f docker-compose-storage.yml run --rm api-deployer /setup.sh
```

If previous command has succeeded, then it is time to build API artifacts and deploy them into the Artifactory:
```
docker-compose -f docker-compose-storage.yml run --rm api-deployer /api-deployer.sh
```

## Building services

After API artifacts is available at Artifactory, it is time to build all the images:
```
docker-compose -f docker-compose.yml -f docker-compose-services.yml pull
docker-compose -f docker-compose.yml -f docker-compose-services.yml build
```

It is also possible to skip this step as `docker-compose` automatically builds images the first time
but after changes has been made, it is necessary to call the `build` command again.

## Deploying services

```
docker-compose -f docker-compose.yml -f docker-compose-services.yml up
```

## Including new service

If a new service is in development, first it is necessary to rely on Artifactory
in order to resolve all the dependencies. Examples of such repository configuration can be found in existing services.
To deploy API of a service, one must edit `api-deployer/api-deployer.sh` and include path to API in proper order in this script.
To build a Docker image, one can rely on examples of `Dockerfile` in other services.

Also, it is important to remember that referencing `localhost` inside of Docker container means only reference to the current container itself and not the host nor any other container. Thus, working with external services, they must be referenced by service name in `docker-compose.yml` files.