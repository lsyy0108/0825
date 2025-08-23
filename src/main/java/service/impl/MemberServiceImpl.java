package service.impl;

import dao.MemberDAO;
import dao.impl.MemberDAOImpl;
import model.Member;
import service.MemberService;

import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements MemberService {

    private final MemberDAO memberDAO = new MemberDAOImpl();

    @Override
    public int createMember(Member member) {
        
        return memberDAO.create(member);
    }

    @Override
    public boolean updateMember(Member member) {
        return memberDAO.update(member);
    }

    @Override
    public boolean deleteMember(int id) {
        return memberDAO.delete(id);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberDAO.findAll();
    }

    @Override
    public Optional<Member> getMemberById(int id) {
        return memberDAO.findById(id);
    }

    @Override
    public Optional<Member> getMemberByMemberCode(String memberCode) {
        return memberDAO.findByMemberCode(memberCode);
    }
}
