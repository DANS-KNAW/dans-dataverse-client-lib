Test Plan
=========

The json files under `lib/src/test/resources` were based on older dataverse versions.
They may not reflect the dataverse API responses of new Dataverse releases.
The smoke tests aim at testing `response.getData()` to parse the API responses.

The calls are mostly grouped by API. Sometimes results of previous calls are used as arguments for subsequent calls.
Such as the ID of a new dataset and files added to that dataset. That causes exceptions in the grouping.

The smoke tests need the same setup as [the examples](https://dans-knaw.github.io/dans-dataverse-client-lib/getting-started/#running-the-examples). Note that the DatasetSmokeTest also calls and admin endpoint.

TODO:
* BuiltInUserApi 
* SwordApi
* TokenApi
* WorkflowsApi
* maybe various methods of other APIs