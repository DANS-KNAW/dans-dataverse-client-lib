Test Plan
=========

The json files under `lib/src/test/resources` were based on older dataverse versions.
They may not reflect the dataverse API responses of new Dataverse releases.
So the provided examples should be tested against a running Dataverse instance after upgrading Dataverse.
Problems with for example `response.getData()` might also break services using the library.

For convenience, a handful of smoke test classes call (most of) the examples.
Mostly grouped by API but file specific examples form a special group.
See also [running the examples](https://dans-knaw.github.io/dans-dataverse-client-lib/getting-started/#running-the-examples).

Look in the code of the test classes for usage logging to prepare the program arguments in the run configurations.
You can find actual ID's for the program arguments by exporting the metadata of a dataset. And the query below.

    select o.identifier, ra.assigneeidentifier, ra.role_id, r.alias, o.dtype 
    from roleassignment ra
    join dvobject o on o.id = definitionpoint_id
    join dataverserole r on ra.role_id = r.id
    order by o.identifier;

Some examples like `DatasetAwaitUnlock` and `DatasetAwaitLock` have comment headers suggesting more test scenario's.
Some examples have optional arguments, these are not used by the smoke tests or are used with some hard coded defaults.

The tests may create drafts or even publish the used dataset.
Delete the draft and/or deaccession the new versions, before running another test class or the same test again.
