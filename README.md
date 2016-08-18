# Anypoint Connector Certification Process 

## Certification Documentation URL
http://mulesoft.github.io/connector-certification-docs/

## Preparing your environment
1. Install ruby.
2. Install bundler: `sudo gem install bundler`.

## Building the docs locally in `develop` 

1. Run `bundle install` to download all the dependencies needed.
2. Run `rake docs:build` (`rake docs:build['basic']` for the basic certification docs)
3. An __HTML__ file will be generated with 2 new folders: __images and files__.
4. Open the __HTML__ generated file and there you go.

## Push changes from `develop` to `gh-pages`
5. After performing the above commands, while still in the `develop` branch, run `rake docs:upload`. This performs a forced update of `gh-pages` remote.
