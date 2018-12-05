# brain-sort-cljs

Comparing brain-sort with drag and drop sorting

## Overview

`brain-sort` introduced me to to idea that there might be a faster way to sort items
that require human comparision (for example prioritizing a TODO list).
See https://github.com/AdrianoFerrari/brain-sort

`brain-sort-cljs` puts a question based sorting implementation side by side with a
drag and drop sorting implementation, to allow for easier comparision of the two approaches.

You can see the built version at https://timothypratley.github.com/brain-sort-cljs/index.html

## Developing

    lein figwheel

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`.

## Deploy

    ./bin/deploy.sh

## License

Copyright © 2018 Timothy Pratley

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
