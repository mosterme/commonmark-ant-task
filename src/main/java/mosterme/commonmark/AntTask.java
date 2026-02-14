package mosterme.commonmark;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Task;

import org.apache.tools.ant.types.FileSet;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files; // Java 11

public class AntTask extends Task
{
    private boolean overwrite, verbose;
    private File source, destination;

    public void setOverwrite(boolean overwrite) {
        this.overwrite = overwrite;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setSource(final File source) {
        this.source = source;
    }

    public void setDestination(final File destination) {
        this.destination = destination;
    }

    // private "internal" commonmark classes that can be re-used
    private final Parser parser = Parser.builder().build();
    private final HtmlRenderer renderer = HtmlRenderer.builder().build();

    protected void toHtml(File in, File out)  {
        if (overwrite || !out.exists() || in.lastModified() > out.lastModified())
        {
            try
            {
                if (verbose) log("\t" + in + " -> " + out);
                String markdown = Files.readString(in.toPath());
                Node node = parser.parse(markdown);
                String content = renderer.render(node);
                Files.writeString(out.toPath(), content);
            }
            catch (IOException e)
            {
                throw new BuildException(e);
            }
        }
    }

    protected void validate() {
        if (source == null) throw new BuildException("source is not set");
        if (destination == null) throw new BuildException("destination is not set");
        if (!source.exists()) throw new BuildException("source does not exist");
        if (!destination.exists()) throw new BuildException("destination does not exist");
        if (!destination.isDirectory()) throw new BuildException("destination is not a directory");
    }

    public void execute() {
        validate();

        if (source.isFile()) {
            log("Converting " + source + " to " + destination);
            File out = new File(destination, source.getName().replace(".md", ".html"));
            toHtml(source, out);
        }

        if (source.isDirectory()) {
            FileSet fileset = new FileSet(); fileset.setDir(source);
            DirectoryScanner scanner = fileset.getDirectoryScanner(getProject());

            log("Converting " + source + " to " + destination + " (" + scanner.getIncludedFilesCount() + " files)");

            for (String element : scanner.getIncludedFiles()) {
                File in = new File(source + File.separator + element);
                File out = new File(destination, in.getName().replace(".md", ".html"));
                toHtml(in, out);
            }
        }
    }
}