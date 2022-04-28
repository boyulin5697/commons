if (redis.call('exists', KEYS[1]) == 1) then
     local lock = tonumber(redis.call('get', KEYS[1]));
     if (lock > 0) then
        redis.call('incrby', KEYS[1], -1);
        return lock;
     end;
      return 0;
  end;
