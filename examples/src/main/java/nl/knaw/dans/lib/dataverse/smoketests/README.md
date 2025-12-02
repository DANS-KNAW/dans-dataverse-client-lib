Test Plan
=========

The json files under `lib/src/test/resources` were based on older dataverse versions.
They may not reflect the dataverse API responses of new Dataverse releases.
The smoke tests aim at testing `response.getData()` to parse the API responses
with classes from `nl.knaw.dans.lib.dataverse.model`.

The calls are mostly grouped by API. Sometimes results of previous calls are used as arguments for subsequent calls.
Such as the ID of a new dataset and files added to that dataset. That causes exceptions in the grouping.

The smoke tests need the same setup as [the examples](https://dans-knaw.github.io/dans-dataverse-client-lib/getting-started/#running-the-examples). Note that the `smoketests.DatasetTest` also calls an admin endpoint. Additionally:
1. Copy the supplied `smoketest.properties.template` to `smoketest.properties`.
2. Edit the properties to match values that exist in the dataverse instance configured in dataverse.properties.


TODO:
* BuiltInUserApi 
* SwordApi
* TokenApi
* WorkflowsApi
* maybe various methods of other APIs
* rewrite tests such that we won't need a smoke test configuration at all, such as:
  * create user with a UUID in its name
  * create a dataverse with a UUID in its name
* Setup that (temporarily) makes DANS specific metadata blocks optional
  for DataverseCreateDataset and DataverseImportDataset examples, introduced in PR #60.