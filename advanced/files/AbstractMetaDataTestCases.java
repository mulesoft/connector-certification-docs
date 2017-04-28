import com.sun.istack.NotNull;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.mule.common.Result;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.tools.devkit.ctf.junit.MetaDataTest;
import org.mule.tools.devkit.ctf.mockup.ConnectorDispatcher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * DataSense test base class that performs several tests over datasense model.
 * It's based on the recoding of persisted JSON metadata representations as golden files than then are compared.
 *
 * The first time that it's executed, golden files are generated. The path will of the file will be printed in the console.
 * These files must be then copied inside test/resources directory and they will be used as the source of truth.
 */
public abstract class AbstractMetaDataTestCases extends AbstractTestCase<T> {

    private final List<String> metadataIds;
    private final String metadataCategory;
    private static final Logger logger = Logger.getLogger(AbstractMetaDataTestCases.class);

    public AbstractMetaDataTestCases(@NotNull final List<String> metadataIds, @NotNull final String metadataCategory) {
        this.metadataIds = metadataIds;
        this.metadataCategory = metadataCategory;
    }

    @MetaDataTest @Test public void verify() throws IOException {

        // Fetch keys and validate that the current set has been returned ...
        final ConnectorDispatcher<T> dispatcher = getDispatcher();
        final Result<List<MetaDataKey>> metaDataKeysResult = dispatcher.fetchMetaDataKeys();
        assertTrue("Unexpected error loading metadata keys", Result.Status.SUCCESS == metaDataKeysResult.getStatus());

        final Map<String, MetaDataKey> keyById = new HashMap<String, MetaDataKey>();
        final List<MetaDataKey> metaDataKeys = metaDataKeysResult.get();
        for (final MetaDataKey metaDataKey : metaDataKeys) {
            keyById.put(metaDataKey.getId(), metaDataKey);
            logger.debug("Datasense key loaded ->" + metaDataKey.getId());
        }

        final File tempDir = createTmpDir();
        for (final String metadataId : metadataIds) {

            // Has the key returned ?
            final MetaDataKey metaDataKey = keyById.get(metadataId);
            assertNotNull("Could not find metadataId " + metadataId, metaDataKey);

            // Validate data structure ...
            final Result<MetaData> metadata = dispatcher.fetchMetaData(metaDataKey);
            assertTrue("Unexpected error  metadata for key" + metadataId, Result.Status.SUCCESS == metadata.getStatus());

            final MetadataSerializer serializer = new MetadataSerializer(metadata.get());
            final String actualResult = serializer.toJson();

            final boolean isFileMissing = compareOrCreate(tempDir, metadataId, actualResult);
            assertFalse("One or more records file could not be found. Review the logs for more information", isFileMissing);

        }
    }

    private boolean compareOrCreate(@NotNull final File tempDir, @NotNull final String metadataId, @NotNull final String actualResult) throws IOException {
        // Has the structure changed ?
        final String outputPath = "datasense" + "/" + metadataCategory;
        final String goldenFile = outputPath + "/" + metadataId + ".json";
        final InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(goldenFile);

        boolean isFileMissing = false;
        if (resourceAsStream == null) {
            // Create output file to store temp file ...
            final File outputFile = new File(tempDir, goldenFile);
            outputFile.getParentFile().mkdirs();

            logger.error(
                    "WARNING: Missing test record file for key " + metadataId + ".Copy the generated files inside test/resources/" + outputPath + ". Generated file " + outputFile);
            System.err
                    .println("Missing test record file for key " + metadataId + ".Copy the generated files inside test/resources/" + outputPath + ". Generated file " + outputFile);

            IOUtils.write(actualResult, new FileOutputStream(outputFile));
            isFileMissing = true;
        } else {
            final String expectedResult = IOUtils.toString(resourceAsStream);
            assertEquals("Changes detected for metadata key " + metadataId + ". Check if this is a regression or remove the golden file to generate it again.", expectedResult,
                    actualResult);
        }
        return isFileMissing;
    }

    @NotNull private File createTmpDir() throws IOException {
        final File tempFile = File.createTempFile("data", "out");
        tempFile.delete();

        final File tempDir = new File(tempFile.getCanonicalPath(), ".dir");
        tempDir.mkdirs();
        return tempDir;
    }
}