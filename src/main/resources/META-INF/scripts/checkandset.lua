local current = redis.call('GET', KEYS[1])
redis.call('SET', KEYS[1], "ku")
if current == ARGV[1] then
    redis.call('SET', KEYS[1], ARGV[2])
    return true
end
return false
---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by liuhaizhan.
--- DateTime: 2018-12-8 0:10
---