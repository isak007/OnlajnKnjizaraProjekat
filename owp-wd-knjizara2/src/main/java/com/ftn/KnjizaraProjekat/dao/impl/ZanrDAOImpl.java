package com.ftn.KnjizaraProjekat.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ftn.KnjizaraProjekat.dao.ZanrDAO;
import com.ftn.KnjizaraProjekat.model.Zanr;

@Repository
@Primary
public class ZanrDAOImpl implements ZanrDAO{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private class ZanrRowMapper implements RowMapper<Zanr> {

		@Override
		public Zanr mapRow(ResultSet rs, int rowNum) throws SQLException {
			int index = 1;
			Long id = rs.getLong(index++);
			String naziv = rs.getString(index++);
			String opis = rs.getString(index++);

			Zanr zanr = new Zanr(id, naziv, opis);
			return zanr;
		}

	}
	@Override
	public Zanr findOne(Long id) {
		String sql = "SELECT id, naziv, opis FROM zanr WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, new ZanrRowMapper(), id);
	}
	@Override
	public List<Zanr> findAll() {
		String sql = "SELECT id, naziv, opis FROM zanr";
		return jdbcTemplate.query(sql, new ZanrRowMapper());
	}
	@Override
	public List<Zanr> find(String naziv) {
		naziv = "%" + naziv + "%";
		String sql = "SELECT id, naziv FROM zanr WHERE naziv LIKE ?";
		return jdbcTemplate.query(sql, new ZanrRowMapper(), naziv);
	}
	@Override
	public int save(Zanr zanr) {
		String sql = "INSERT INTO zanr (naziv, opis) VALUES (?,?)";
		return jdbcTemplate.update(sql, zanr.getNaziv(), zanr.getOpis());
	}
	
	@Override
	public int [] save(ArrayList<Zanr> zanrovi) {
		String sql = "INSERT INTO zanr (naziv, opis) VALUES (?,?)";
		
		return jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setString(1, zanrovi.get(i).getNaziv());
						ps.setString(2, zanrovi.get(i).getOpis());
					}
					
					@Override
					public int getBatchSize() {
						return zanrovi.size();
					}
				});
	}
	
	@Override
	public int update(Zanr zanr) {
		String sql = "UPDATE zanr SET naziv = ?, opis = ? WHERE id = ?";
		return jdbcTemplate.update(sql, zanr.getNaziv(), zanr.getOpis(), zanr.getId());
	}
	@Override
	public int delete(Long id) {
		String sql = "DELETE FROM zanr WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}
}
