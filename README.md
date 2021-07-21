The scheduler allows adding tasks that will be executed in the given date. 

You can add or remove tasks via API that can be looked at http://localhost:5009/swagger-ui.html after application starting.

On the start application gets tasks, that should be executed in the future, from DB and add them to scheduler.
The failed tasks, that should be executed in past but were not for some reason, are executed again.

Every hour past tasks that were not executed are run.