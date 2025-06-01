package org.springframe.backend.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframe.backend.constants.BlackListConst;
import org.springframe.backend.constants.RedisConst;
import org.springframe.backend.domain.entity.BlackList;
import org.springframe.backend.repository.BlackListRepository;
import org.springframe.backend.service.BlackListService;
import org.springframe.backend.utils.RedisCache;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@RequiredArgsConstructor
@Service
public class BlackListServiceImpl implements BlackListService {
    private final BlackListRepository blackListRepository;
    private final RedisCache redisCache;


    @Transactional
    @Override
    public ResponseResult<Void> deleteBlackList(List<Long> ids) {
        List<BlackList> blackLists = blackListRepository.findAllById(ids);
        for (BlackList blackList : blackLists) {
            if(blackList.getType() == BlackListConst.BLACK_LIST_TYPE_BOT){
                redisCache.delCacheMapValue(RedisConst.BLACK_LIST_IP_KEY,blackList.getIpInfo().getCreateIp());
            } else if(blackList.getType() == BlackListConst.BLACK_LIST_TYPE_USER){
                redisCache.delCacheMapValue(RedisConst.BLACK_LIST_IP_KEY,blackList.getUserId().toString());
            }
        }
        try{
            blackListRepository.deleteAllById(ids);
            return ResponseResult.Success();
        } catch (Exception e) {
            return ResponseResult.Fail();
        }
    }

    @Override
    public ResponseResult<Void> addBlackList(BlackList blackList) {
        return null;
    }

    @Transactional
    @Override
    public ResponseResult<Void> updateBlackList(BlackList blackList) {
        return null;
    }
}
