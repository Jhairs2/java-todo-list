package io.github.jhairs2.todo_list.todo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.jhairs2.todo_list.todo.dto.ProjectListDTO;
import io.github.jhairs2.todo_list.todo.exceptions.ProjectListNotFoundException;
import io.github.jhairs2.todo_list.todo.mapper.ProjectListDTOMapper;
import io.github.jhairs2.todo_list.todo.model.ProjectList;
import io.github.jhairs2.todo_list.todo.model.TodoUser;
import io.github.jhairs2.todo_list.todo.repository.ProjectListRepository;
import jakarta.transaction.Transactional;

@Service
public class ProjectListService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectListService.class);

    private final ProjectListRepository projectListRepository;
    private final ProjectListDTOMapper projectListDTOMapper;
    private final HelperService helperService;

    public ProjectListService(ProjectListRepository projectListRepository, ProjectListDTOMapper projectListDTOMapper,
            HelperService helperService) {
        this.projectListRepository = projectListRepository;
        this.projectListDTOMapper = projectListDTOMapper;
        this.helperService = helperService;

    }

    public List<ProjectListDTO> getAllProjectLists() {
        Long userId = this.helperService.getActiveUserId();
        String username = this.helperService.getActiveUsername();
        logger.info("Retrieving {}'s ProjectLists from DB...", username);
        return this.projectListDTOMapper
                .convertToDTOList(this.projectListRepository.findAllByUserId(userId));

    }

    public ProjectListDTO getProjectList(Long projectListId) {
        Long userId = this.helperService.getActiveUserId();
        String username = this.helperService.getActiveUsername();
        logger.info("Retrieving {}'s ProjectList {} from DB...", username, projectListId);
        return this.projectListDTOMapper.convertToDTO(
                this.projectListRepository.findByUserIdAndId(userId, projectListId)
                        .orElseThrow(() -> new ProjectListNotFoundException(
                                "Project with id: " + projectListId + " can not be found.")));
    }

    public ProjectListDTO createNewProjectList(ProjectListDTO projectListDTO) {
        TodoUser user = this.helperService.getActiveUser();
        String username = this.helperService.getActiveUsername();

        logger.info("Adding new project to DB for User {}...", username);
        ProjectList projectList = new ProjectList(projectListDTO.listTitle());

        logger.info("Linking user and project...");
        projectList.setUser(user);
        user.getProjects().add(projectList);

        logger.info("Saving project to DB...");
        return this.projectListDTOMapper.convertToDTO(this.projectListRepository.save(projectList));
    }

    @Transactional
    public ProjectListDTO updateProjectList(Long projectListId, ProjectListDTO projectListDTO) {
        Long userId = this.helperService.getActiveUserId();
        logger.info("Looking for project {} to update...", projectListId);
        ProjectList newProject = this.projectListRepository.findByUserIdAndId(userId, projectListId)
                .orElseThrow(() -> new ProjectListNotFoundException(
                        "Project with id: " + projectListId + " can not be found."));

        logger.info("Project {} found. Updating list {}...", projectListId, newProject.getListTitle());
        newProject.setListTitle(projectListDTO.listTitle());

        logger.info("Project {} updated. Saving project to DB...", projectListId);
        return this.projectListDTOMapper.convertToDTO(this.projectListRepository.save(newProject));
    }

    public ProjectListDTO deleteProjectList(Long projectListId) {
        Long userId = this.helperService.getActiveUserId();

        logger.info("Looking for project {} to delete...", projectListId);
        ProjectList deletedProject = this.projectListRepository
                .findByUserIdAndId(userId, projectListId)
                .orElseThrow(() -> new ProjectListNotFoundException("Project with that id can not be found."));

        logger.info("Project {} found. Deleting from DB...", projectListId);
        this.projectListRepository.delete(deletedProject);

        return this.projectListDTOMapper.convertToDTO(deletedProject);
    }

}
