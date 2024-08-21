package io.bizbee.onechat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.bizbee.onechat.server.common.entity.Group;
import io.bizbee.onechat.server.common.vo.user.GroupInviteReq;
import io.bizbee.onechat.server.common.vo.user.GroupMemberResp;
import io.bizbee.onechat.server.common.vo.user.GroupVO;

import java.util.List;

public interface IGroupService extends IService<Group> {

  GroupVO createGroup(String groupName);

  GroupVO modifyGroup(GroupVO vo);

  void deleteGroup(Long groupId);

  void quitGroup(Long groupId);

  void kickGroup(Long groupId, Long userId);

  List<GroupVO> findGroups();

  void invite(GroupInviteReq vo);

  Group findBaseInfoById(Long groupId);

  GroupVO findById(Long groupId);

  List<GroupMemberResp> findGroupMembers(Long groupId);

  List<Group> findByGroupType(Integer code);
}
