package org.springframe.backend.service;

import org.springframe.backend.domain.entity.BlackList;
import org.springframe.backend.utils.ResponseResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlackListService  {
    ResponseResult<Void> addBlackList(BlackList blackList);

    ResponseResult<Void> deleteBlackList(List<Long> ids);

    ResponseResult<Void> updateBlackList(BlackList blackList);


}
