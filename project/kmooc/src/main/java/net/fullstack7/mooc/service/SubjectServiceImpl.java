package net.fullstack7.mooc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import net.fullstack7.mooc.domain.Subject;
import net.fullstack7.mooc.repository.SubjectRepository;

import java.util.List;

@Service
@Log4j2
public class SubjectServiceImpl implements SubjectService {

  @Autowired
  private SubjectRepository subjectRepository;

  @Override
  public List<Subject> getAllSubjects() {
    log.info("getAllSubjects 호출");
    return subjectRepository.findAllByOrderBySubjectIdAsc();
  }
}
