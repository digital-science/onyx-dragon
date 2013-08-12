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

You need a bunch of environment variables defined in order for the OnyxDragon to 
work, something like the following:

    export AWS_KEY="your_aws_access_key"
    export AWS_SEC="your_aws_secret_access_key"
    export DISTRIBUTION_ID="your_cloudfront_distribution_id"

If you want notifications in Campfire:

    export CAMPFIRE_ROOM="the_name_of_your_campfire_room"
    export CAMPFIRE_TOKEN="your_campfire_account_secret_api_token"
    export CAMPFIRE_SUBDOMAIN="your_campfire_subdomain"

Once those environment variables are set up, you can run:

    lein run

## License

Copyright © 2013 Digital Science

Distributed under the Eclipse Public License, the same as Clojure.
