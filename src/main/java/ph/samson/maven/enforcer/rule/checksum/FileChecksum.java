/*
 * Copyright 2015 Edward Samson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ph.samson.maven.enforcer.rule.checksum;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;

/**
 * Enforcer Rule to verify checksum of a file.
 */
public class FileChecksum implements EnforcerRule {

    private File file;
    private String checksum;
    private String type = "sha1";

    public File getFile() {
        return file;
    }

    /**
     * The file to check.
     *
     * @param file file
     */
    public void setFile(File file) {
        this.file = file;
    }

    public String getChecksum() {
        return checksum;
    }

    /**
     * The expected checksum value.
     *
     * @param checksum checksum
     */
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getType() {
        return type;
    }

    /**
     * The checksum algorithm to use.
     *
     * Default: "sha1".
     * Possible values: "crc32", "crc32c", "md5", "sha1", "sha256", "sha512".
     *
     * @param type algorithm
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void execute(EnforcerRuleHelper erh) throws EnforcerRuleException {
        if (file == null || !file.canRead()) {
            throw new EnforcerRuleException("Missing file: " + file);
        }

        HashFunction hashFn;
        switch (type) {
            case "crc32":
                hashFn = Hashing.crc32();
                break;
            case "crc32c":
                hashFn = Hashing.crc32c();
                break;
            case "md5":
                hashFn = Hashing.md5();
                break;
            case "sha1":
                hashFn = Hashing.sha1();
                break;
            case "sha256":
                hashFn = Hashing.sha256();
                break;
            case "sha512":
                hashFn = Hashing.sha512();
                break;
            default:
                throw new EnforcerRuleException(
                        "Unsupported hash type: " + type);
        }

        String hash;
        try {
            hash = hashFn.hashBytes(Files.readAllBytes(file.toPath()))
                    .toString();
        } catch (IOException ex) {
            throw new EnforcerRuleException("Failed reading " + file, ex);
        }

        if (!hash.equalsIgnoreCase(checksum)) {
            throw new EnforcerRuleException(type + " hash of " + file + " was "
                    + hash + " but expected " + checksum);
        }
    }

    @Override
    public boolean isCacheable() {
        return false;
    }

    @Override
    public boolean isResultValid(EnforcerRule er) {
        return false;
    }

    @Override
    public String getCacheId() {
        return "no cache";
    }

}
