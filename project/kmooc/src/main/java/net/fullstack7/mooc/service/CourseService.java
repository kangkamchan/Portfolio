package net.fullstack7.mooc.service;

import jakarta.persistence.EntityNotFoundException;
import net.fullstack7.mooc.domain.*;
import net.fullstack7.mooc.dto.*;
import net.fullstack7.mooc.util.FileUploadUtil;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.fullstack7.mooc.repository.*;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CourseService {
  private final CourseRepository courseRepository;
  private final LectureRepository lectureRepository;
  private final LectureContentRepository lectureContentRepository;
  private final LectureFileRepository lectureFileRepository;
  private final QuizRepository quizRepository;
  private final SubjectRepository subjectRepository;
  private final FileUploadUtil fileUploadUtil;
  private final InstitutionRepository institutionRepository;
  private final TeacherRepository teacherRepository;
  private final ModelMapper modelMapper;

  
  public Course createCourse(CourseCreateDTO dto, Teacher teacher) throws IOException {
    // 과목 조회
    Subject subject = subjectRepository.findBySubjectId(dto.getSubjectId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 과목입니다."));

    // 썸네일 업로드
    String thumbnailPath = fileUploadUtil.uploadImageFile(dto.getThumbnail(), "thumbnails");

    Course course = Course.builder()
        .title(dto.getTitle())
        .subject(subject)
        .weeks(dto.getWeeks())
        .learningTime(dto.getLearningTime())
        .language(dto.getLanguage())
        .description(dto.getDescription())
        .isCreditBank(dto.getIsCreditBank())
        .thumbnail(thumbnailPath.replace("\\","/"))
        .teacher(teacher)
        .status("DRAFT")
        .viewCount(0)
        .createdAt(LocalDateTime.now())
        .build();

    return courseRepository.save(course);
  }

  //권한 체크
  public boolean checkAuthority(int id, Teacher teacher,String type) {
    switch (type) {
      case "course":
        return courseRepository.findById(id).get().getTeacher().getTeacherId().equals(teacher.getTeacherId());
      case "lecture":
        return lectureRepository.findById(id).get().getCourse().getTeacher().getTeacherId().equals(teacher.getTeacherId());
      case "content":
        return lectureContentRepository.findById(id).get().getLecture().getCourse().getTeacher().getTeacherId().equals(teacher.getTeacherId());
      case "quiz":
        return quizRepository.findById(id).get().getLecture().getCourse().getTeacher().getTeacherId().equals(teacher.getTeacherId());
      default:
        log.info("알맞은 권한 체크 타입이 아닙니다.");
        return false;
    }
  }


  public Lecture createLecture(LectureCreateDTO dto) {
    Course course = courseRepository.findById(dto.getCourseId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강좌입다."));

    Lecture lecture = Lecture.builder()
        .title(dto.getTitle())
        .description(dto.getDescription())
        .course(course)
        .build();

    return lectureRepository.save(lecture);
  }

  @Transactional
  public LectureContent createContent(LectureContentCreateDTO dto) throws IOException {
    Lecture lecture = lectureRepository.findById(dto.getLectureId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 섹션입니다."));

    // video나 file 타입 검증
    if (dto.getFile() == null) {
      throw new IllegalArgumentException("파일은 필수입니다.");
    }

    // LectureContent 생성
    LectureContent content = LectureContent.builder()
        .title(dto.getTitle())
        .description(dto.getDescription())
        .lecture(lecture)
        .isVideo("video".equals(dto.getType()) ? 1 : 0)
        .build();

    LectureContent savedContent = lectureContentRepository.save(content);

    // 파일 처리
    String filePath = dto.getType().equals("video")
        ? fileUploadUtil.uploadVideoFile(dto.getFile(), "videos")
        : fileUploadUtil.uploadDocumentFile(dto.getFile(), "documents");

    saveLectureFile(savedContent, dto.getFile(), filePath.replace("\\","/"));

    return savedContent;
  }

  private void saveLectureFile(LectureContent content, MultipartFile file, String filePath) {
    LectureFile lectureFile = LectureFile.builder()
        .fileName(file.getOriginalFilename())
        .filePath(filePath.replace("\\","/"))
        .fileExtension(FilenameUtils.getExtension(file.getOriginalFilename()))
        .lectureContent(content)
        .build();

    lectureFileRepository.save(lectureFile);
    log.info("강의 파일 정보 저장 완료: {}", lectureFile.getLectureFileId());
  }

  @Transactional
  public void updateLecture(int lectureId, LectureUpdateDTO dto) {
    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 섹션입니다."));

    lecture.setTitle(dto.getTitle());
    lecture.setDescription(dto.getDescription());

    lectureRepository.save(lecture);
  }

  @Transactional
  public void updateContent(int contentId, LectureContentUpdateDTO dto) throws IOException {
    LectureContent content = lectureContentRepository.findById(contentId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠입니다."));

    content.setTitle(dto.getTitle());
    content.setDescription(dto.getDescription());
    content.setIsVideo("video".equals(dto.getType()) ? 1 : 0);

    if (dto.getFile() != null) {
      LectureFile oldFile = lectureFileRepository.findByLectureContent(content)
          .orElse(null);
      if (oldFile != null) {
        fileUploadUtil.deleteFile(oldFile.getFilePath());
        lectureFileRepository.delete(oldFile);
      }

      String filePath = dto.getType().equals("video")
          ? fileUploadUtil.uploadVideoFile(dto.getFile(), "videos")
          : fileUploadUtil.uploadDocumentFile(dto.getFile(), "documents");
      saveLectureFile(content, dto.getFile(), filePath.replace("\\","/"));
    }

    lectureContentRepository.save(content);
  }

  @Transactional
  public void deleteContent(int contentId) {
    try {
      LectureContent content = lectureContentRepository.findById(contentId)
          .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 콘텐츠입니다."));

      LectureFile file = lectureFileRepository.findByLectureContent(content).orElse(null);
      if (file != null) {
        fileUploadUtil.deleteFile(file.getFilePath());
        lectureFileRepository.delete(file);
      }

      lectureContentRepository.delete(content);
    } catch (Exception e) {
      log.error("콘텐츠 삭제 실패", e);
      throw new RuntimeException("콘텐츠 삭제에 실패했습니다.", e);
    }
  }

  public void deleteQuizzes(int lectureId) {
    Lecture lecture = lectureRepository.findByLectureId(lectureId)
        .orElseThrow(() -> new IllegalArgumentException("존제하지 않는 강좌입니다."));
    quizRepository.deleteAllByLecture(lecture);
  }

  // 읽기 전용 트랜잭션 (CUD방지 및 내부최적화)
  @Transactional(readOnly = true)
  public CourseDetailDTO getCourseWithContents(int courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강좌입니다."));

    List<LectureDTO> lectureDTOs = course.getLectures().stream()
        .map(lecture -> {

          List<Quiz> quizzes = quizRepository.findByLectureOrderByQuizIdAsc(lecture);
          log.info("Lecture ID: {}, Quizzes: {}", lecture.getLectureId(), quizzes);
          log.info("QuizzesSize:{}", quizzes.size());
          // 콘텐츠 매핑
          List<LectureContentDTO> contentDTOs = Optional.ofNullable(lecture.getContents())
              .orElse(Collections.emptyList())
              .stream()
              .map(content -> LectureContentDTO.builder()
                  .lectureContentId(content.getLectureContentId())
                  .title(content.getTitle())
                  .description(content.getDescription().replaceAll("[\r\n]+","<br>"))
                  .isVideo(content.getIsVideo())
                  .type(content.getIsVideo() == 1 ? "video" : "file")
                  .file(content.getLectureFile() != null ? LectureFileDTO.builder()
                      .lectureFileId(content.getLectureFile().getLectureFileId())
                      .fileName(content.getLectureFile().getFileName())
                      .filePath(content.getLectureFile().getFilePath())
                      .fileExtension(content.getLectureFile().getFileExtension())
                      .build()
                      : null)
                  .build())
              .collect(Collectors.toList());

          // 퀴즈 매핑
          List<QuizDTO> quizDTOs = quizzes.stream()
              .map(quiz -> QuizDTO.builder()
                  .quizId(quiz.getQuizId())
                  .question(quiz.getQuestion())
                  .answer(quiz.getAnswer())
                  .build())
              .collect(Collectors.toList());

          return LectureDTO.builder()
              .lectureId(lecture.getLectureId())
              .title(lecture.getTitle())
              .description(lecture.getDescription().replaceAll("[\r\n]+","<br>"))
              .contents(contentDTOs)
              .quizzes(quizDTOs)
              .build();
        })
        .collect(Collectors.toList());

    return CourseDetailDTO.builder()
        .courseId(course.getCourseId())
        .title(course.getTitle())
        .description(course.getDescription().replaceAll("[\r\n]+","<br>"))
        .thumbnail(course.getThumbnail())
        .weeks(course.getWeeks())
        .learningTime(course.getLearningTime())
        .language(course.getLanguage())
        .isCreditBank(course.getIsCreditBank())
        .teacherId(course.getTeacher().getTeacherId())
        .teacherName(course.getTeacher().getTeacherName())
        .subjectId(course.getSubject().getSubjectId())
        .status(course.getStatus())
        .lectures(lectureDTOs)
        .build();
  }

  @Transactional
  public void createQuizzes(QuizCreateDTO dto) {
    Lecture lecture = lectureRepository.findById(dto.getLectureId())
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 섹션입니다."));

    if (dto.getQuizzes() == null || dto.getQuizzes().isEmpty()) {
      throw new IllegalArgumentException("최소 하나의 퀴즈는 필수입니다.");
    }

    // quizRepository.deleteAllByLecture(lecture);

    // 새 퀴즈 생성
    dto.getQuizzes().forEach(quizDTO -> {
      Quiz quiz = Quiz.builder()
          .question(quizDTO.getQuestion())
          .answer(quizDTO.getAnswer())
          .lecture(lecture)
          .build();
      quizRepository.save(quiz);
    });
  }

  @Transactional
  public void updateQuiz(int quizId, QuizDTO dto) {
    Quiz quiz = quizRepository.findById(quizId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 퀴즈입니다."));

    quiz.setQuestion(dto.getQuestion());
    quiz.setAnswer(dto.getAnswer());

    quizRepository.save(quiz);
  }

  @Transactional
  public void deleteQuiz(int quizId) {
    quizRepository.deleteById(quizId);
  }

  @Transactional
  public Page<CourseResponseDTO> getCourses(CourseSearchDTO courseSearchDTO) {
    Page<CourseResponseDTO> courses = courseRepository.coursePage(courseSearchDTO.getPageable(), courseSearchDTO, null,
        -1);
    courseSearchDTO.setTotalCount((int) courses.getTotalElements());
    courses = courseRepository.coursePage(courseSearchDTO.getPageable(), courseSearchDTO, null, -1);
    return courses;
  }

  public List<Institution> getInstitutions() {
    return institutionRepository.findAllByOrderByInstitutionIdAsc();
  }

  public List<Subject> getSubjects() {
    return subjectRepository.findAll();
  }

  public CourseDTO getCourseById(int courseId) {
    return modelMapper.map(courseRepository.getReferenceById(courseId), CourseDTO.class);
  }

    public CourseViewDTO getCourseViewById(int courseId) {
    try {
      Course course = courseRepository.getReferenceById(courseId);
      CourseViewDTO courseViewDTO = modelMapper.map(course, CourseViewDTO.class);
      List<CourseDTO> recommendations = courseRepository
              .findBySubjectAndStatusOrderByCourseIdDesc(courseViewDTO.getSubject(), "PUBLISHED", PageRequest.of(0, 5))
              .stream().filter(c -> c.getCourseId() != courseViewDTO.getCourseId())
              .map(c -> modelMapper.map(c, CourseDTO.class)).toList();
      courseViewDTO.setRecommendations(recommendations);
      return courseViewDTO;
    }catch(EntityNotFoundException e) {
      log.info("데이터없음");
      log.error(e.getMessage());
      return null;
    }catch(Exception e){
      log.error(e.getMessage());
      return null;
    }
  }

  @Transactional
  public void deleteLecture(int lectureId) {
    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 섹션입니다."));

    List<LectureContent> contents = lectureContentRepository.findByLecture(lecture);
    // 콘텐츠 삭제
    for (LectureContent content : contents) {
      LectureFile file = lectureFileRepository.findByLectureContent(content).orElse(null);
      //파일 삭제
      if (file != null) {
        fileUploadUtil.deleteFile(file.getFilePath());
        lectureFileRepository.delete(file);
      }
      lectureContentRepository.delete(content);
    }

    // 퀴즈 삭제
    quizRepository.deleteAllByLecture(lecture);
    // 섹션 삭제
    lectureRepository.delete(lecture);
  }

  @Transactional
  public void updateCourse(int courseId, CourseUpdateDTO dto, Teacher teacher) throws IOException {
    Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강좌입니다."));

    if (!course.getTeacher().getTeacherId().equals(teacher.getTeacherId())) {
      throw new IllegalArgumentException("수정 권한이 없습니다.");
    }

    Subject subject = subjectRepository.findById(dto.getSubjectId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 과목입니다."));

    if (dto.getThumbnail() != null) {
      fileUploadUtil.deleteFile(course.getThumbnail());
      String thumbnailPath = fileUploadUtil.uploadImageFile(dto.getThumbnail(), "thumbnails");
      course.setThumbnail(thumbnailPath);
    }

    course.setTitle(dto.getTitle());
    course.setDescription(dto.getDescription());
    course.setSubject(subject);
    course.setWeeks(dto.getWeeks());
    course.setLearningTime(dto.getLearningTime());
    course.setLanguage(dto.getLanguage());
    course.setIsCreditBank(dto.getIsCreditBank());

    courseRepository.save(course);
  }

  public List<CourseResponseDTO> mainCourseList(int n) {
    return courseRepository.randomCourses(n);
  }

  @Transactional
  public void deleteCourse(int courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 강좌입니다."));

    List<Lecture> lectures = lectureRepository.findAllByCourse(course);
    for (Lecture lecture : lectures) {
      deleteLecture(lecture.getLectureId());
    }
    courseRepository.delete(course);
  }

//상태 체크
  public boolean checkPublished(int Id,String type) {
    switch (type) {
      case "course":
        return courseRepository.findById(Id).get().getStatus().equals("PUBLISHED");
      case "lecture":
        return lectureRepository.findById(Id).get().getCourse().getStatus().equals("PUBLISHED");
      case "quiz":
        return quizRepository.findById(Id).get().getLecture().getCourse().getStatus().equals("PUBLISHED");
      case "content":
        return lectureContentRepository.findById(Id).get().getLecture().getCourse().getStatus().equals("PUBLISHED");
      default:
        log.info("알맞은 상태 체크 타입이 아닙니다.");
        return false;
    }
  }
  public Course getCourseByLectureContentId(int lectureContentId) {
    return lectureContentRepository.findById(lectureContentId).get().getLecture().getCourse();
  }
}
