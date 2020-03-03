#!/usr/bin/env bash

# Password come from an env and that is why this has to be wrapped in bash
# There is no access to the system env from mongo script

set -e

mongo <<EOF
use gumtree
db.createUser({
  user: 'gumtree',
  pwd: '$GUMTREE_DB_PASSWORD',
  roles: [{
    role: 'readWrite',
    db: 'gumtree'
  }]
})
EOF