package net.cheney.motown.resource.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.cheney.motown.api.Method;
import net.cheney.motown.resource.api.Resource;

import org.apache.commons.io.FileUtils;

public class FileResource extends Resource {

	private final File file;
	private final FileResourceProvidor providor;

	FileResource(FileResourceProvidor providor, File file) {
		this.providor = providor;
		this.file = file;
	}

	public boolean exists() {
		return file().exists();
	}

	public Date lastModified() {
		return new Date(file().lastModified());
	}

	public String etag() {
		return String.format("\"%d\"", file().lastModified()); // "2134123412"
	}

	public ByteBuffer entity() throws IOException {
		FileInputStream fis = new FileInputStream(file());
		FileChannel fc = fis.getChannel();
		try {
			return fc.map(MapMode.READ_ONLY, 0, fc.size());
		} finally {
			fc.close();
		}
	}

	public boolean mkcol() {
		return file().mkdir();
	}

	public FileResource parent() {
		return new FileResource(providor, file().getParentFile());
	}

	public void put(ByteBuffer entity) throws IOException {
		FileChannel fc = new FileOutputStream(file()).getChannel();
		fc.write(entity);
		fc.close();
	}

	public boolean delete() {
		return (isCollection() ? deleteDirectory() : deleteFile());
	}

	private boolean deleteDirectory() {
		try {
			FileUtils.deleteDirectory(file());
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private boolean deleteFile() {
		return file().delete();
	}

	public boolean isCollection() {
		return file().isDirectory();
	}

	public File file() {
		return file;
	}

	public List<Resource> members() {
		File[] files = file.listFiles();
		List<Resource> children = new ArrayList<Resource>();
		for(File c : (files == null ? new File[0] : files )) {
			children.add(new FileResource(providor, c));
		}
		return children;
	}

	public void copyTo(final Resource destination) throws IOException {
		File dest = ((FileResource)destination).file;
		File source = this.file;
		
		if(source.isDirectory()) {
			if(dest.isDirectory()) {
				FileUtils.copyDirectoryToDirectory(source, dest);
			} else {
				dest.delete();
				FileUtils.copyDirectory(source, dest);
			}
		} else {
			if(dest.isDirectory()) {
				FileUtils.copyFileToDirectory(source, dest);
			} else {
				FileUtils.copyFile(source, dest);
			}
		}
	}
	
	public void moveTo(final Resource destination) throws IOException {
		File dest = ((FileResource)destination).file;
		File source = this.file;
		
		if(source.isDirectory()) {
			if(dest.isDirectory()) {
				FileUtils.copyDirectoryToDirectory(source, dest);
				FileUtils.deleteDirectory(source);
			} else {
				dest.delete();
				FileUtils.copyDirectory(source, dest);
				FileUtils.deleteDirectory(source);
			}
		} else {
			if(dest.isDirectory()) {
				FileUtils.copyFileToDirectory(source, dest);
				source.delete();
			} else {
				FileUtils.copyFile(source, dest);
				source.delete();
			}
		}
	}
	
	public long size() {
		return file.length();
	}
	
	public String displayName() {
		return file.getName();
	}
	
	@Override
	public boolean equals(Object o) {
		return (o instanceof FileResource && ((FileResource)o).file.equals(this.file));
	}
	
	@Override
	public int hashCode() {
		return file.hashCode();
	}

	public Collection<Method> supportedMethods() {
		return Arrays.asList(Method.values());
	}

	public Collection<ComplianceClass> davOptions() {
		return Arrays.asList(new ComplianceClass[] { ComplianceClass.LEVEL_1, ComplianceClass.LEVEL_2 });
	}

	public FileResourceProvidor providor() {
		return providor;
	}

}
