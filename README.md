# Anypoint Connector Certification Process 

## Certification Documentation URL
http://mulesoft.github.io/connector-certification-docs/

## Preparing your environment
1. Install ruby.
2. Install bundler: `sudo gem install bundler`.

## Building the docs locally and push to `gh-pages`

1. Run `bundle install` to download all the dependencies needed.
2. Run `rake docs:build` (`rake docs:build['basic']` for the basic certification docs)
3. An __HTML__ file will be generated with 2 new folders: __images and files__.
4. Open the __HTML__ generated file and there you go.
5. You can then perform the necessary git steps to update the `gh-pages` branch.
