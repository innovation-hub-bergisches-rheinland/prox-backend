insert into prox_user.search_preferences (id, user_id, tag_collection_id)
  (
    SELECT gen_random_uuid(),
           up.user_id,
           up.tag_collection_id FROM prox_user.user_profile up
    WHERE up.tag_collection_id IS NOT NULL
  );