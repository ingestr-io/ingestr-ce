# Ingestr CE (Community Edition)

This is the Community Edition (Open Source) version of the Ingestr Loader application which is gives all of the Ingestor
Framework functionalities to Loaders.

It is automatically used by any new Loader initalised via the Ingestr CLI tool for creating a new Loader.

If the Ingestr CLI is already installed, use the `extractor init` command to initialize a new Loader.

## Documentation

To read the full documentation about the Ingestr framework go to the [Documentation](https://extractor.io/docs/latest) on
the Ingestr.io website.

### Running integration tests

You can run integration tests by executing `mvn verify`

### Debugging

To debug the plugin, you first need to publish a snapshot to your Maven local:

```shell
$ mvn install
```

Then you need a sample application. The one at `examples/java` is the most up-to-date, but you can in principle generate
a new one from Micronaut Starter. Then, change its `pom.xml` to set the following property:
  