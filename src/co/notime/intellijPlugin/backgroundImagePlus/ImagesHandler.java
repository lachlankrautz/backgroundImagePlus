package co.notime.intellijPlugin.backgroundImagePlus;

import org.bouncycastle.asn1.x509.NoticeReference;

import javax.activation.MimetypesFileTypeMap;
import javax.management.Notification;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.codehaus.plexus.util.FileUtils.getExtension;

/**
 * Author: Allan de Queiroz
 * Date:   07/05/17
 */
class ImagesHandler {

    /**
     * @param folder folder to search for images
     * @return random image or null
     */
    String getRandomImage(String folder,String tmpFile) {
        if (folder.isEmpty()) {
            return null;
        }
        try(FindImagesVisitor imageFinder = new FindImagesVisitor(!(tmpFile == null || tmpFile.isEmpty()))) {
            Path rootSearch = Paths.get(folder);
            Files.walkFileTree(rootSearch,imageFinder);
            List<Path> images = imageFinder.getImages();
            if(images.isEmpty()) {
                NotificationCenter.notice("No Images found in: "+folder);
                return null;
            }
            Path imagePath = images.get((int)(Math.random() * images.size()));
            if(imageFinder.isInZip(imagePath)) {
                //should never be null because zips should not be added if tmpFile is null.
                assert tmpFile != null;
                Path zipSavePath = Paths.get(tmpFile);
                Files.createDirectories(zipSavePath);
                Files.list(zipSavePath).filter(((Predicate<Path>)Files::isDirectory).negate()).forEach(this::deleteFile);
                Path result = zipSavePath.resolve(imagePath.getFileName().toString());
                Files.copy(imagePath, result , REPLACE_EXISTING);
                return result.toAbsolutePath().toString();
            }
            else
                return imagePath.toAbsolutePath().toString();
        }catch(IOException ioe) {
            NotificationCenter.notice("Image couldn't be opened: "+ioe.getMessage());
            ioe.printStackTrace();
            return null;
        }
    }

    private void deleteFile(Path path) {
        try {
            Files.deleteIfExists(path);
        }catch(IOException ioe) {
            NotificationCenter.notice("Couldn't delete file: "+path);
        }
    }

}
