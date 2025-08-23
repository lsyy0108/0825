package dao;

import model.Member;
import java.util.List;
import java.util.Optional;

public interface MemberDAO {
    int create(Member member);
    boolean update(Member member);
    boolean delete(int id);
    List<Member> findAll();
    Optional<Member> findById(int id);
    Optional<Member> findByMemberCode(String memberCode);
}
