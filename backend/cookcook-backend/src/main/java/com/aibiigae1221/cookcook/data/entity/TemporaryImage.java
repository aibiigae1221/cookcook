package com.aibiigae1221.cookcook.data.entity;

import java.util.Date;

import org.hibernate.annotations.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Check(constraints = "status in ('used', 'unused')") // mysql에서 check 제약사항 적용이 안됨 ㅠㅠ...
@Entity
@Table
public class TemporaryImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imageId;
	
	@Column(nullable = false, unique = true)
	private String imageFileName;
	
	// unique 값 줘야하지만 인덱스 여러개 생성되는건 좀 부담되니 걍 냅둠..
	@Column(nullable = false) 
	private String imageLocalPath;
	
	@Temporal(TemporalType.DATE)
	private Date createdAt;
	
	@Column(name="status", nullable = false)
	private String status;
	
	public TemporaryImage() {}
	
	public TemporaryImage(String imageFileName, String imageLocalPath, Date createdAt, String status) {
		super();
		this.imageFileName = imageFileName;
		this.imageLocalPath = imageLocalPath;
		this.createdAt = createdAt;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}


	public String getImageLocalPath() {
		return imageLocalPath;
	}

	public void setImageLocalPath(String imageLocalPath) {
		this.imageLocalPath = imageLocalPath;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	
}
