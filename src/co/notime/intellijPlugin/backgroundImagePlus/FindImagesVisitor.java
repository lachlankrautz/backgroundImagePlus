package co.notime.intellijPlugin.backgroundImagePlus;

import javax.activation.MimetypesFileTypeMap;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class FindImagesVisitor extends SimpleFileVisitor<Path> implements Closeable {
    private MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
    private Set<FileSystem> openFileSystems = new HashSet<>();
    private List<Path> results = new ArrayList<>();
    private boolean visitZips;

    public FindImagesVisitor(boolean visitZips) {
        this.visitZips = visitZips;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(isImage(file.toString())) {
            processImage(file);
        }else if(typeMap.getContentType(file.toString()).equals("application/zip")) {
            processZip(file);
        }
        return FileVisitResult.CONTINUE;
    }

    private boolean isImage(String file) {
        String[] parts = typeMap.getContentType(file).split("/");
        return parts.length != 0 && parts[0].equals("image");
    }

    private void processImage(Path file) {
        results.add(file);
    }

    private void processZip(Path file) throws IOException{
        if(openFileSystems.contains(file.getFileSystem()) || !visitZips)
            return;
        FileSystem system = FileSystems.newFileSystem(file,getClass().getClassLoader());
        openFileSystems.add(system);
        for(Path root:system.getRootDirectories())
            Files.walkFileTree(root,this);
    }

    @Override
    public void close() throws IOException {
        results.clear();
        for(FileSystem system : openFileSystems)
            system.close();
        openFileSystems.clear();
    }

    public List<Path> getImages() {
        return Collections.unmodifiableList(results);
    }

    public boolean isInZip(Path path) {
        return openFileSystems.contains(path.getFileSystem());
    }

}
