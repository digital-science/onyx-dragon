# OnyxDragon

OnyxDragon monitors your CloudFront distribution and lets you know when
invalidations are in progress (and when they've finshed).

## What problem does this solve?

If part of your deployment involves invalidating resources on the CloudFront
CDN, then you can only consider the deployment complete when the invalidation
has completed (is no longer in progress). To find this out you could refresh
some home-grown status page, or roll your own invalidation check, but a bot that
informs you of state changes (none inprogress -> invalidations in progress and
back to none) reduces the busy time to nothing!

## Usage

    lein run

## License

Copyright © 2013 Digital Science

Distributed under the Eclipse Public License, the same as Clojure.
