# ilyab

generated using Luminus version "2.9.12.02"

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

This project was generated with:
```bash
$ lein new luminus ilyab +re-frame +datomic
```

Then Bootstrap was updated to "4.0.0-beta.2" and the Bootstrap required JavaScript was added to home.html, otherwise everything worked out of the box.

## Running

To start a web server for the application, run:

```bash
$ lein run
```

To compile and watch the ClojureScript, run:

```bash
$ lein figwheel
```

## Deployment

The app runs on Heroku and can be deployed like this:

```bash
# deploy the master branch
git push heroku master

# deploy a feature branch (replace feature-branch with your branch)
git push heroku feature-branch:master
```

Heroku builds the application and runs the command specified in the `Procfile`.

## License

Copyright © 2017 Ilya Bernshteyn
