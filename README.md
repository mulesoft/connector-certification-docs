# Anypoint Connector Certification Process 

## Complete Certification Documentation URL
http://mulesoft.github.io/complete-connector-certification-docs

## Basic Certification Documentation URL
http://mulesoft.github.io/basic-connector-certification-docs 

## Preparing your environment
1. Install ruby.
2. Install bundler: `sudo gem install bundler`.	

## Building the docs locally:
<<<<<<< Updated upstream
1. Run `bundle install` to download all the dependencies needed.
2. Run `rake docs:build` (`rake docs:build['basic']` for the basic certification docs) 
3. An __HTML__ file will be generated with 2 new folders: __images and files__.
4. Open the __HTML__ generated file and there you go.
=======
1. Go to `[your_path]/connector-certification-docs/current` folder.
2. Run the following command: `asciidoctor -a stylesheet=assets/mule.css user-manual.adoc`.
3. Open the generated html file: `[your_path]/connector-certification-docs/current/user-manual.html`.
4. Be amazed.
>>>>>>> Stashed changes
