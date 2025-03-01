package com.example.taskplanner.service;

import com.example.taskplanner.model.Folder;
import com.example.taskplanner.repository.FolderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {
    private final FolderRepository folderRepository;

    public FolderService(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    public Folder createFolder(Folder folder) {
        return folderRepository.save(folder);
    }

    public void deleteFolder(Long id) {
        folderRepository.deleteById(id);
    }

    public List<Folder> getFoldersByUserId(Long userId) {
        return folderRepository.findByUserId(userId); // ✅ Теперь возвращает папки ТОЛЬКО текущего пользователя
    }
}

