namespace :docs do
  desc 'prepare build'
  task :prebuild do
    Dir.mkdir 'images' unless Dir.exists? 'images'
    Dir.glob("docs/*/images/*").each do |image|
      FileUtils.copy(image, "images/" + File.basename(image))
    end

    Dir.mkdir 'files' unless Dir.exists? 'files'
    Dir.glob("docs/*/files/*").each do |file|
      FileUtils.copy(file, "files/" + File.basename(file))
    end

    Dir.glob("files/*.asc").each do |file|
      `bundle exec asciidoctor #{file}`
    end
  end


  task :build => :prebuild do

    puts "Converting to HTML..."
    `bundle exec asciidoctor certification-docbook.asc -a stylesheet=theme/style.css`
    puts " -- HTML output at certification-docbook.html"

  end

  task :buildBasicCert => :prebuild do

    puts "Converting to HTML..."
    `bundle exec asciidoctor basic-certification-docbook.asc -a stylesheet=theme/style.css`
    puts " -- HTML output at basic-certification-docbook.html"

  end

  desc 'push generated documents to the repository'
  task :upload => :build do
    puts "Uploading generated documentation"
    `git checkout gh-pages -f`
    `git add *.html && git add images/ && git add files/ && git commit -m 'Updated documentation'`
    `git push origin gh-pages -f`
    `git checkout develop -f`

  end

  desc 'clean out generated formats'
  task :clean do
    `rm *.html`
    `rm -rf images`
    `rm -rf files`
  end

end
