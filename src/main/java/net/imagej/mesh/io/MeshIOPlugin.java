/*-
 * #%L
 * I/O plugins for ImageJ meshes.
 * %%
 * Copyright (C) 2016 - 2018 University of Idaho, Royal Veterinary College, and
 * Board of Regents of the University of Wisconsin-Madison.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imagej.mesh.io;

import net.imagej.mesh.Mesh;

import org.scijava.io.IOPlugin;

/**
 * A plugin which reads and/or writes {@link Mesh} objects.
 *
 * @author Curtis Rueden
 * @author Kyle Harrington (University of Idaho, Moscow)
 */
public interface MeshIOPlugin extends IOPlugin<Mesh> {

	String extension();

	@Override
	default Class<Mesh> getDataType() {
		return Mesh.class;
	}

	@Override
	public boolean supportsOpen(final Location location) {
		if (!(location instanceof FileLocation)) return false;
		final FileLocation fileLocation = (FileLocation) location;
		final String source = fileLocation.getFile().getAbsolutePath();
		return FileUtils.getExtension(source).toLowerCase().equals(EXTENSION);
	}

	@Override
	public boolean supportsSave(final Location location) {
		if (!(location instanceof FileLocation)) return false;
		final FileLocation fileLocation = (FileLocation) location;
		final String destination = fileLocation.getFile().getAbsolutePath();
		return FileUtils.getExtension(destination).toLowerCase().equals(EXTENSION);
	}

	@Override
	public Mesh open(final Location location) throws IOException {
		if (!(location instanceof FileLocation)) {
			throw new UnsupportedOperationException("Not a file: " + location.getClass().getName());
		}
		final FileLocation fileLocation = (FileLocation) location;
		final Mesh mesh = new NaiveFloatMesh();
		read(fileLocation.getFile(), mesh);
		return mesh;
	}

	@Override
	public void save(final Mesh data, final Location location) throws IOException {
		if (!(location instanceof FileLocation)) {
			throw new UnsupportedOperationException("Not a file: " + location.getClass().getName());
		}
		final FileLocation fileLocation = (FileLocation) location;
		final byte[] bytes = writeBinary(data);
		FileUtils.writeFile(fileLocation.getFile(), bytes);
	}
}
