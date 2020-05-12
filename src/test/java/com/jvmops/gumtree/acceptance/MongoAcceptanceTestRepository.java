package com.jvmops.gumtree.acceptance;

import com.jvmops.gumtree.notifications.model.Ad;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface MongoAcceptanceTestRepository extends MongoRepository<Ad, ObjectId> {
    @Query(value = """
            {
                description: {
                    $regex:?0 
                }
            }""")
    List<Ad> findAllByRegexpOverDescription(String inDescription);
    @Query(value = """
            { $and: 
                [{
                    description: {
                        $regex:?0
                    }
                },
                {
                    description: {
                        $regex:?1
                    }
                }]
            }""")
    List<Ad> findAllByRegexpInDescriptionAndRegexpNotInDescription(String inDescription, String notInDescription);
}
