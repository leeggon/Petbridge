package site.petbridge.domain.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.petbridge.domain.board.domain.Board;
import site.petbridge.domain.board.dto.request.BoardEditRequestDto;
import site.petbridge.domain.board.dto.request.BoardRegistRequestDto;
import site.petbridge.domain.board.dto.response.BoardResponseDto;
import site.petbridge.domain.board.repository.BoardRepository;
import site.petbridge.domain.user.domain.User;
import site.petbridge.global.exception.ErrorCode;
import site.petbridge.global.exception.PetBridgeException;
import site.petbridge.util.AuthUtil;
import site.petbridge.util.FileUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final AuthUtil authUtil;
    private final FileUtil fileUtil;

    /**
     * 게시글 등록
     */
    @Transactional
    @Override
    public void registBoard(BoardRegistRequestDto boardRegistRequestDto, MultipartFile thumbnailFile) throws Exception {
        User user = authUtil.getAuthenticatedUser();

        String savedThumbnailFileName = null;
        if (thumbnailFile != null) {
            savedThumbnailFileName = fileUtil.saveFile(thumbnailFile, "boards");
        }

        Board entity = boardRegistRequestDto.toEntity(user.getId(),savedThumbnailFileName);
        boardRepository.save(entity);
    }

    /**
     * 게시글 목록 조회
     */
    @Override
    public List<BoardResponseDto> getListBoard(int page, int size, String userNickname, String title) throws Exception {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        return boardRepository.findAllByUserNickNameAndTitleContains(userNickname, title, pageable).getContent();
    }

    /**
     * 게시글 상세 조회
     */
    @Override
    public BoardResponseDto getDetailBoard(int id) throws Exception {
        BoardResponseDto boardResponseDto = boardRepository.getDetailBoardById(id);
        if (boardResponseDto == null) {
            throw new PetBridgeException(ErrorCode.RESOURCES_NOT_FOUND);
        }

        return boardResponseDto;
    }

    /**
     * 게시글 수정
     */
    @Transactional
    @Override
    public void editBoard(int id, BoardEditRequestDto boardEditRequestDto, MultipartFile thumbnailFile) throws Exception {
        User user = authUtil.getAuthenticatedUser();

        // 없거나 삭제된 게시판 404
        Board entity = boardRepository.findByIdAndDisabledFalse(id)
                .orElseThrow(() -> new PetBridgeException(ErrorCode.RESOURCES_NOT_FOUND));
        // 내 게시판 아님 403
        if (entity.getUserId() != user.getId()) {
            throw new PetBridgeException(ErrorCode.FORBIDDEN);
        }

        String savedThumbnailFileName = null;
        if (thumbnailFile != null) {
            savedThumbnailFileName = fileUtil.saveFile(thumbnailFile, "boards");
        }

        entity.update(boardEditRequestDto, savedThumbnailFileName);
        boardRepository.save(entity);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    @Override
    public void removeBoard(int id) throws Exception {
        User user = authUtil.getAuthenticatedUser();

        // 없거나 삭제된 게시판 404
        Board entity = boardRepository.findByIdAndDisabledFalse(id)
                .orElseThrow(() -> new PetBridgeException(ErrorCode.RESOURCES_NOT_FOUND));
        // 내 게시판 아님 403
        if (entity.getUserId() != user.getId()) {
            throw new PetBridgeException(ErrorCode.FORBIDDEN);
        }

        entity.disable();
        boardRepository.save(entity);
    }
}
