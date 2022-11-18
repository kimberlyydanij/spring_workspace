package part01_mybatis;

import java.util.List;
import org.mybatis.spring.SqlSessionTemplate;

public class MemDaoImp implements MemDAO{
	
	private SqlSessionTemplate sqlSession;
	
	public MemDaoImp() {
	
	}

	 public void setSqlSession(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}
	 
	@Override
	public List<MemDTO> list() {
		return sqlSession.selectList("mem.all");
	}
	
	@Override
	public void insertMethod(MemDTO dto) {
		sqlSession.insert("mem.ins",dto);
	}
	
	@Override
	public void deleteMethod(int num) {
			
	}
	
	@Override
	public MemDTO updateMethod(int num) {
		return null;
	}
	
	@Override
	public void updateMethod(MemDTO dto) {
	}
	
	@Override
	public MemDTO one(int num) {
		return sqlSession.selectOne("mem.one", num);
	}
	
	@Override
	public int countMethod() {
		return 0;
	}

} // end class
