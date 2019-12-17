#!/usr/bin/env bash

mongo "$rootAuthDatabase" <<-EOJS
        use gumtree
				db.createUser({
					user: $(jq --arg 'user' "$MONGO_INITDB_DATABASE" --null-input '$user'),
					pwd: $(jq --arg 'pwd' "$MONGO_INITDB_ROOT_PASSWORD" --null-input '$pwd'),
					roles: [ { role: 'readWrite', db: $(jq --arg 'db' "$MONGO_INITDB_DATABASE" --null-input '$db') } ]
				})
			EOJS
