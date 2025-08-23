package service;

import model.Member;
import java.util.List;
import java.util.Optional;

public interface MemberService {
    int createMember(Member member);
    boolean updateMember(Member member);
    boolean deleteMember(int id);
    List<Member> getAllMembers();
    Optional<Member> getMemberById(int id);
    Optional<Member> getMemberByMemberCode(String memberCode);
}
