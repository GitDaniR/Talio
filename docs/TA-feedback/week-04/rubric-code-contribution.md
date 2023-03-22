#### Focused Commits
_Very Good_

The repository had a good amount of commits last week which is nice to see. The commit messages are mostly all good, except for things that should be more detailed like: "Edited test method names" (commit af263af6), which could be more detailed into why/where ->(Edited test method names in _file_ for _consitency_). Another example is: "Added service" in commit 16ad94ba, which should be something like "added _serviceName_ which handles _job_". 
Lastly commit sizes are mostly fine, with a few outliners on both sides. Ofcourse small things like needing to change 1 line for checkstyle is something that happens during coding, so that's fine, but some of the really big commits could be nice to break down. (like commit 1516a1be "An intermediate stage in experimenting with cards"). Othwerwise I am really impressed, keep it up!


#### Isolation
_Very Good_

There a a decent amount of succesfull MR's merged to main, and the size of them are all good. Each MR has a clear focus and scope. There are some really small MR (like !60), but that's fine as bugs happen from time to time.

#### Reviewability
_Good_

MR's have a clear focus, good title and good/coherent changes. Sometimes the MR don't have descriptions, which should be a standard. Really impressed by the progress so far, keep it up!


#### Code Reviews
_Very Good_

Most MR's have comments, which led to discussions and iterative changes to the branch, which is really good to see. There are some MR's without any comments, like !59 and !60, (which I understand as they are bugfixes/refactoring), but even a single LGTM! comments is better than nothing.


#### Build Server
_Excellent_

There are no failing branches on GitLab. THere are frequent pushes which activate the build server, which is nice to see, and when a build fails, it is always immediatly fixed!! There are also 10+ checkstyle rules present. Really good job!

