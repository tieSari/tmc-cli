package wad.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wad.domain.FileObject;
import wad.domain.Image;
import wad.repository.FileObjectRepository;
import wad.repository.ImageRepository;
import wad.service.imaging.Thumbnailer;

@Service
public class ImageService {

    @Autowired
    private FileObjectRepository fileObjectRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private Thumbnailer thumbnailer;

    @Async
    public void add(Image image, String mediaType, String filename, byte[] data) throws IOException {
        FileObject original = new FileObject();
        original.setContentType(mediaType);
        original.setContent(data);
        original.setName(filename);
        original.setContentLength(new Long(data.length));

        original = fileObjectRepository.save(original);

        FileObject thumbnail = null;
        try {
            thumbnailer.process(data, filename);
            thumbnail = fileObjectRepository.save(thumbnail);
        } catch (Throwable t) {
            // not a nice way to handle this -- we're doing this
            // to simplify testing of the exercise
        }

        image.setOriginal(original);
        image.setThumbnail(thumbnail);

        imageRepository.save(image);
    }
}
