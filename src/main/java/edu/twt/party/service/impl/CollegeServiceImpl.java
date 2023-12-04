package edu.twt.party.service.impl;

import edu.twt.party.dao.CollegeMapper;
import edu.twt.party.pojo.College;
import edu.twt.party.service.CollegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeServiceImpl implements CollegeService {
    @Autowired
    CollegeMapper collegeMapper;
    @Override
    public List<College> getColleges() {

        return collegeMapper.getColleges();
    }
}
