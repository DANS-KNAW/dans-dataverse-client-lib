Getting started
===============

Basic usage
-----------

For including the library as a dependency in a Maven project, see [the installation instructions](./index.md#using-the-library)

A basic usage example follows:

```java

public class Test {

    public static void main(String[] args) throws Exception {
        // Create a client object
        DataverseClientConfig config = new DataverseClientConfig(
            new URI("http://localhost:8080/"),
            "your-api-token-here");
        DataverseClient client = new DataverseClient(config);

        // Call an API end-point
        DataverseResponse<Dataverse> response = client.dataverse("root").view();

        // Retrieve response data as model object
        Dataverse dv = response.getData();

        // Query object
        System.out.println(dv.getDescription());
    }

}
```

1. First, you need to create a client object. This object can be reused when calling the same Dataverse instance multiple times.
2. The API end-points are grouped in roughly the same way as they appear in the Dataverse documentation: dataverse collection end-points, dataset end-points,
   etc.
3. The result is a `DataverseHttpResponse` object, if successful, otherwise an exception.
4. If Dataverse's response is a JSON document, this is converted to a model object, a simple value object. You can retrieve that from the response using the
   `getData` method.

Running the examples
--------------------

More examples can be found in the [examples submodule](https://github.com/DANS-KNAW/dans-dataverse-client-lib/tree/master/examples/){:target=_blank:}. To run
an example:

1. Copy the supplied `dataverse.properties.template` to `dataverse.properties`.
2. Edit the properties to match your setup. For example; when running Dataverse on localhost, set the `baseUrl=http://localhost:8080`.
3. Run [one of the programs]{:target=_blank} providing sensible command line parameters where required.
4. Note that if you are accessing admin end-points from a different machine, you may need to configure the host running Dataverse to accept API calls from  
   other hosts. You can do this by setting the `BlockedApiKey` and `BlockedApiPolicy` settings to empty values using curl:

       curl -X PUT -d s3kretKey http://localhost:8080/api/admin/settings/:BlockedApiKey
       curl -X PUT -d unblock-key http://localhost:8080/api/admin/settings/:BlockedApiPolicy

[one of the programs]: https://github.com/DANS-KNAW/dans-dataverse-client-lib/tree/master/examples/src/main/java/nl/knaw/dans/lib/dataverse/example

Smoke tests
-----------

The "examples" submodule also contains a package "smoketest." The classes in this package can be used to run smoke tests against a running Dataverse instance.
They are intended to verify that the library works as expected with the specific Dataverse version. So, they are more useful for library developers than for
library users.